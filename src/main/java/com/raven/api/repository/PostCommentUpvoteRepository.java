package com.raven.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raven.api.model.PostCommentUpvote;

public interface PostCommentUpvoteRepository extends JpaRepository<PostCommentUpvote, Long> {

    Optional<PostCommentUpvote> findByPostCommentIdAndUserId(Long postCommentId, Long userId);
    
}
