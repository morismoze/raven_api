package com.raven.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raven.api.model.PostView;

public interface PostViewRepository extends JpaRepository<PostView, Long> {
    
}
