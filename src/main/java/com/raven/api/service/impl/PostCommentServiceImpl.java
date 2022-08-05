package com.raven.api.service.impl;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.model.Post;
import com.raven.api.model.PostComment;
import com.raven.api.model.PostCommentDownvote;
import com.raven.api.model.PostCommentUpvote;
import com.raven.api.model.User;
import com.raven.api.repository.PostCommentRepository;
import com.raven.api.service.PostCommentDownvoteService;
import com.raven.api.service.PostCommentService;
import com.raven.api.service.PostCommentUpvoteService;

import java.sql.Timestamp;
import java.util.Optional;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;

    private final PostCommentUpvoteService postCommentUpvoteService;

    private final PostCommentDownvoteService postCommentDownvoteService;

    private final MessageSourceAccessor accessor;

    @Override
    public PostComment createPostComment(final Post post, final User user, final String comment) {
        final PostComment postComment = new PostComment();
        
        postComment.setPost(post);
        postComment.setUser(user);
        postComment.setComment(comment);
        postComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        postComment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return this.postCommentRepository.save(postComment);
    }

    @Override
    public Page<PostComment> getPageablePostComments(final Post post, final Integer page, final Integer limit) {
        final Sort sort = Sort.by("postCommentUpvotes").ascending().and(Sort.by("createdAt").descending());
        final Pageable pageable = PageRequest.of(page, limit, sort);
        return this.postCommentRepository.findAllByPostId(post.getId(), pageable);
    }

    @Override
    @Transactional
    public Integer upvotePostComment(final Long id, final User user) {
        final Optional<PostComment> postCommentOptional = this.postCommentRepository.findById(id);

        if (postCommentOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postComment.notFound", new Object[]{id}));
        }
        
        final PostComment postComment = postCommentOptional.get();
        try {
            PostCommentUpvote postCommentUpvote = this.postCommentUpvoteService.findByPostCommentIdAndUserId(id, user.getId());
            this.postCommentUpvoteService.deleteById(postCommentUpvote.getId());
            this.postCommentRepository.save(postComment);
            return postComment.getPostCommentUpvotes().size() - postComment.getPostCommentDownvotes().size();
        } catch (EntryNotFoundException e) {
            // user hasn't upvoted the comment, so create a new one
            final PostCommentUpvote postCommentUpvote = this.postCommentUpvoteService.createNewPostCommentUpvote(postComment, user);
            postComment.getPostCommentUpvotes().add(postCommentUpvote);
            return postComment.getPostCommentDownvotes().size() - postComment.getPostCommentDownvotes().size();
        }
    }

    @Override
    @Transactional
    public Integer downvotePostComment(final Long id, final User user) {
        final Optional<PostComment> postCommentOptional = this.postCommentRepository.findById(id);

        if (postCommentOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postComment.notFound", new Object[]{id}));
        }
        
        final PostComment postComment = postCommentOptional.get();
        try {
            PostCommentDownvote postCommentDownvote = this.postCommentDownvoteService.findByPostCommentIdAndUserId(id, user.getId());
            this.postCommentDownvoteService.deleteById(postCommentDownvote.getId());
            return postComment.getPostCommentDownvotes().size() - postComment.getPostCommentDownvotes().size();
        } catch (EntryNotFoundException e) {
            // user hasn't downvoted the comment, so create a new one
            final PostCommentDownvote postCommentDownvote = this.postCommentDownvoteService.createNewPostCommentDownvote(postComment, user);
            postComment.getPostCommentDownvotes().add(postCommentDownvote);
            return postComment.getPostCommentDownvotes().size() - postComment.getPostCommentDownvotes().size();
        }         
    }
    
}
