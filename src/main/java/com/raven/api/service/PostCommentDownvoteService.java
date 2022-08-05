package com.raven.api.service;

import org.springframework.stereotype.Service;

import com.raven.api.model.PostComment;
import com.raven.api.model.PostCommentDownvote;
import com.raven.api.model.User;
import com.raven.api.service.PostCommentDownvoteService;

@Service
public interface PostCommentDownvoteService {

    PostCommentDownvote createNewPostCommentDownvote(PostComment postComment, User user);

    PostCommentDownvote findByPostCommentIdAndUserId(Long postCommentId, Long userId);

    void deleteById(Long id);

}
