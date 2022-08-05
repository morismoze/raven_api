package com.raven.api.service.impl;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.model.PostComment;
import com.raven.api.model.PostCommentDownvote;
import com.raven.api.model.User;
import com.raven.api.repository.PostCommentDownvoteRepository;
import com.raven.api.service.PostCommentDownvoteService;

import java.sql.Timestamp;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommentDownvoteServiceImpl implements PostCommentDownvoteService {

    private final PostCommentDownvoteRepository postCommentDownvoteRepository;

    private final MessageSourceAccessor accessor;

    @Override
    public PostCommentDownvote createNewPostCommentDownvote(PostComment postComment, User user) {
        final PostCommentDownvote postCommentDownvote = new PostCommentDownvote();

        postCommentDownvote.setPostComment(postComment);
        postCommentDownvote.setUser(user);
        postCommentDownvote.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        postCommentDownvote.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        
        return this.postCommentDownvoteRepository.save(postCommentDownvote);
    }

    @Override
    public PostCommentDownvote findByPostCommentIdAndUserId(Long postCommentId, Long userId) {
        Optional<PostCommentDownvote> postCommentDownvoteOptional = this.postCommentDownvoteRepository
            .findByPostCommentIdAndUserId(postCommentId, userId);
        
        if (postCommentDownvoteOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postCommentDownvote.notFound", new Object[]{postCommentId, userId}));
        }

        return postCommentDownvoteOptional.get();
    }

    @Override
    public void deleteById(Long id) {
        this.postCommentDownvoteRepository.deleteById(id);
    }
    
}
