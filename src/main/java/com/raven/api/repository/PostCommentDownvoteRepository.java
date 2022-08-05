package com.raven.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raven.api.model.PostCommentDownvote;

public interface PostCommentDownvoteRepository extends JpaRepository<PostCommentDownvote, Long> {
    
    Optional<PostCommentDownvote> findByPostCommentIdAndUserId(Long postCommentId, Long userId);

}
