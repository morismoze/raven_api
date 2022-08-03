package com.raven.api.service.impl;

import org.springframework.stereotype.Service;

import com.raven.api.model.Post;
import com.raven.api.model.PostComment;
import com.raven.api.model.User;
import com.raven.api.repository.PostCommentRepository;
import com.raven.api.service.PostCommentService;

import java.sql.Timestamp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;

    @Override
    public PostComment createPostComment(Post post, User user, String comment) {
        final PostComment postComment = new PostComment();
        
        postComment.setPost(post);
        postComment.setUser(user);
        postComment.setComment(comment);
        postComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        postComment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return this.postCommentRepository.save(postComment);
    }
    
}
