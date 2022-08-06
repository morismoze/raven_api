package com.raven.api.service;

import org.springframework.stereotype.Service;

import com.raven.api.model.Post;
import com.raven.api.model.PostUpvote;
import com.raven.api.model.User;

@Service
public interface PostUpvoteService {

    PostUpvote createPostUpvote(Post post, User user);

    PostUpvote findByPostIdAndUserId(Long postId, Long userId);

    void deleteById(Long id);

}
