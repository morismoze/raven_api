package com.raven.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raven.api.model.PostUpvote;

public interface PostUpvoteRepository extends JpaRepository<PostUpvote, Long> {

    Optional<PostUpvote> findByPostIdAndUserId(Long postId, Long userId);
    
}
