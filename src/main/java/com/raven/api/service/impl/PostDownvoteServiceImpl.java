package com.raven.api.service.impl;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.model.Post;
import com.raven.api.model.PostDownvote;
import com.raven.api.model.User;
import com.raven.api.repository.PostDownvoteRepository;
import com.raven.api.service.PostDownvoteService;

import java.sql.Timestamp;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostDownvoteServiceImpl implements PostDownvoteService {

    private final PostDownvoteRepository postDownvoteRepository;

    private final MessageSourceAccessor accessor;

    @Override
    public PostDownvote createPostDownvote(Post post, User user) {
        final PostDownvote postDownvote = new PostDownvote();

        postDownvote.setPost(post);
        postDownvote.setUser(user);
        postDownvote.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        postDownvote.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        
        return this.postDownvoteRepository.save(postDownvote);
    }

    @Override
    public PostDownvote findByPostIdAndUserId(Long postId, Long userId) {
        Optional<PostDownvote> postDownvoteOptional = this.postDownvoteRepository.findByPostIdAndUserId(postId, userId);
        if (postDownvoteOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postDownvote.notFound", new Object[]{postId, userId}));
        }

        return postDownvoteOptional.get();
    }

    @Override
    public void deleteById(Long id) {
        this.postDownvoteRepository.deleteById(id);         
    }
    
}
