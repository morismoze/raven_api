package com.raven.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raven.api.model.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    
}
