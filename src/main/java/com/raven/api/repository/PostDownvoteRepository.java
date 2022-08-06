package com.raven.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raven.api.model.PostDownvote;

public interface PostDownvoteRepository extends JpaRepository<PostDownvote, Long> {

    Optional<PostDownvote> findByPostIdAndUserId(Long postId, Long userId);
    
}
