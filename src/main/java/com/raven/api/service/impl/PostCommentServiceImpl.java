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
import com.raven.api.model.PostCommentReportReason;
import com.raven.api.model.PostCommentUpvote;
import com.raven.api.model.User;
import com.raven.api.repository.PostCommentRepository;
import com.raven.api.service.PostCommentDownvoteService;
import com.raven.api.service.PostCommentReportService;
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

    private final PostCommentReportService postCommentReportService;

    private final PostCommentUpvoteService postCommentUpvoteService;

    private final PostCommentDownvoteService postCommentDownvoteService;

    private final MessageSourceAccessor accessor;

    @Override
    public PostComment getPostComment(Long id) {
        final Optional<PostComment> postCommentOptional = this.postCommentRepository.findById(id);

        if (postCommentOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postComment.notFound", new Object[]{id}));
        }
        
        return postCommentOptional.get();
    }

    @Override
    @Transactional
    public PostComment createPostComment(final Post post, final User user, final String comment) {
        final PostComment postComment = new PostComment();
        
        postComment.setPost(post);
        postComment.setUser(user);
        postComment.setComment(comment);
        postComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        postComment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        final PostComment savedPostComment = this.postCommentRepository.save(postComment);
        post.getPostComments().add(savedPostComment);

        return savedPostComment;
    }

    @Override
    public Page<PostComment> findPageablePostComments(final Post post, final Integer page, final Integer limit) {
        final Sort sort = Sort.by("postCommentUpvotes").ascending().and(Sort.by("createdAt").descending());
        final Pageable pageable = PageRequest.of(page, limit, sort);
        
        return this.postCommentRepository.findAllByPostId(post.getId(), pageable);
    }

    @Override
    @Transactional
    public Integer upvotePostComment(final Long id, final User user) {
        final PostComment postComment = this.getPostComment(id);

        try {
            final PostCommentUpvote postCommentUpvote = this.postCommentUpvoteService.findByPostCommentIdAndUserId(id, user.getId());
            // user upvoted already upvoted post comment, so remove the upvote
            this.postCommentUpvoteService.deleteById(postCommentUpvote.getId());
            return postComment.getPostCommentUpvotes().size() - postComment.getPostCommentDownvotes().size();
        } catch (EntryNotFoundException entryNotFoundExceptionUpvote) {
            // user hasn't upvoted the comment, so create a new one
            try {
                PostCommentDownvote postCommentDownvote = this.postCommentDownvoteService.findByPostCommentIdAndUserId(id, user.getId());
                // user prevously downvoted the post comment, so remove the downvote
                this.postCommentDownvoteService.deleteById(postCommentDownvote.getId());
            } catch (EntryNotFoundException entryNotFoundExceptionDownvote) {
                // user hasn't previously downvoted the comment
            }

            // create new upvote
            this.postCommentUpvoteService.createPostCommentUpvote(postComment, user);
            return postComment.getPostCommentUpvotes().size() - postComment.getPostCommentDownvotes().size();
        }
    }

    @Override
    @Transactional
    public Integer downvotePostComment(final Long id, final User user) {
        final PostComment postComment = this.getPostComment(id);

        try {
            final PostCommentDownvote postCommentDownvote = this.postCommentDownvoteService.findByPostCommentIdAndUserId(id, user.getId());
            // user downvoted already downvoted post comment, so remove the downvote
            this.postCommentDownvoteService.deleteById(postCommentDownvote.getId());
            return postComment.getPostCommentUpvotes().size() - postComment.getPostCommentDownvotes().size();
        } catch (EntryNotFoundException entryNotFoundExceptionDownvote) {
            // user hasn't downvoted the comment, so create a new one
            try {
                final PostCommentUpvote postCommentUpvote = this.postCommentUpvoteService.findByPostCommentIdAndUserId(id, user.getId());
                // user prevously upvoted the post comment, so remove the upvote
                this.postCommentUpvoteService.deleteById(postCommentUpvote.getId());
            } catch (EntryNotFoundException entryNotFoundExceptionUpvote) {
                // user hasn't previously upvoted the comment
            }

            // create new downvote
            this.postCommentDownvoteService.createPostCommentDownvote(postComment, user);
            return postComment.getPostCommentUpvotes().size() - postComment.getPostCommentDownvotes().size();
        }         
    }

    @Override
    public void reportPostComment(User user, Long id, String description, PostCommentReportReason postCommentReportReason) {        
        final Optional<PostComment> postCommentOptional = this.postCommentRepository.findById(id);
        if (postCommentOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postComment.notFound", new Object[]{id}));
        }

        this.postCommentReportService.createPostCommentReport(postCommentOptional.get(), user, description, postCommentReportReason);        
    }

    @Override
    public void editPostComment(Long id, String comment) {
        Optional<PostComment> postCommentOptional = this.postCommentRepository.findById(id);    
        if (postCommentOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postComment.notFound", new Object[]{id}));
        }
        
        PostComment postComment = postCommentOptional.get();
        postComment.setComment(comment);
        this.postCommentRepository.save(postComment);
    }

    @Override
    public void deletePostComment(Long id) {
        Optional<PostComment> postCommentOptional = this.postCommentRepository.findById(id);    
        if (postCommentOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postComment.notFound", new Object[]{id}));
        }
        
        this.postCommentRepository.deleteById(id);
    }
    
}
