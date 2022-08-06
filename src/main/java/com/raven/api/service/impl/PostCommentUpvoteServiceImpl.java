package com.raven.api.service.impl;

import java.util.Optional;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.model.PostComment;
import com.raven.api.model.PostCommentUpvote;
import com.raven.api.model.User;
import com.raven.api.repository.PostCommentUpvoteRepository;
import com.raven.api.service.PostCommentUpvoteService;

import java.sql.Timestamp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommentUpvoteServiceImpl implements PostCommentUpvoteService {

    private final PostCommentUpvoteRepository postCommentUpvoteRepository;

    private final MessageSourceAccessor accessor;

    @Override
    public PostCommentUpvote createPostCommentUpvote(PostComment postComment, User user) {
        final PostCommentUpvote postCommentUpvote = new PostCommentUpvote();

        postCommentUpvote.setPostComment(postComment);
        postCommentUpvote.setUser(user);
        postCommentUpvote.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        postCommentUpvote.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        
        return this.postCommentUpvoteRepository.save(postCommentUpvote);
    }

    @Override
    public PostCommentUpvote findByPostCommentIdAndUserId(Long postCommentId, Long userId) {
        Optional<PostCommentUpvote> postCommentUpvoteOptional = this.postCommentUpvoteRepository
            .findByPostCommentIdAndUserId(postCommentId, userId);
        if (postCommentUpvoteOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postCommentUpvote.notFound", new Object[]{postCommentId, userId}));
        }

        return postCommentUpvoteOptional.get();
    }

    @Override
    public void deleteById(Long id) {
        this.postCommentUpvoteRepository.deleteById(id);       
    }
 
}
