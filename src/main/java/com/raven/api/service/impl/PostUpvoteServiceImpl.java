package com.raven.api.service.impl;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.model.Post;
import com.raven.api.model.PostUpvote;
import com.raven.api.model.User;
import com.raven.api.repository.PostUpvoteRepository;
import com.raven.api.service.PostUpvoteService;

import java.sql.Timestamp;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostUpvoteServiceImpl implements PostUpvoteService {

    private final PostUpvoteRepository postUpvoteRepository;

    private final MessageSourceAccessor accessor;

    @Override
    public PostUpvote createPostUpvote(Post post, User user) {
        final PostUpvote postUpvote = new PostUpvote();

        postUpvote.setPost(post);
        postUpvote.setUser(user);
        postUpvote.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        postUpvote.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        
        return this.postUpvoteRepository.save(postUpvote);
    }

    @Override
    public PostUpvote findByPostIdAndUserId(Long postId, Long userId) {
        Optional<PostUpvote> postUpvoteOptional = this.postUpvoteRepository.findByPostIdAndUserId(postId, userId);
        if (postUpvoteOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postUpvote.notFound", new Object[]{postId, userId}));
        }

        return postUpvoteOptional.get();
    }

    @Override
    public void deleteById(Long id) {
        this.postUpvoteRepository.deleteById(id);         
    }
    
}
