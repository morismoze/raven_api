package com.raven.api.service;

import org.springframework.stereotype.Service;

import com.raven.api.model.Post;
import com.raven.api.model.PostDownvote;
import com.raven.api.model.User;

@Service
public interface PostDownvoteService {

    PostDownvote createPostDownvote(Post post, User user);

    PostDownvote findByPostIdAndUserId(Long postId, Long userId);

    void deleteById(Long id);

}
