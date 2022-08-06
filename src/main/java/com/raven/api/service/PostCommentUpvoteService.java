package com.raven.api.service;

import org.springframework.stereotype.Service;

import com.raven.api.model.PostComment;
import com.raven.api.model.PostCommentUpvote;
import com.raven.api.model.User;
import com.raven.api.service.PostCommentUpvoteService;

@Service
public interface PostCommentUpvoteService {

    PostCommentUpvote createPostCommentUpvote(PostComment postComment, User user);

    PostCommentUpvote findByPostCommentIdAndUserId(Long postCommentId, Long userId);

    void deleteById(Long id);

}
