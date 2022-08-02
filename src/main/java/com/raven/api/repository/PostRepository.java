package com.raven.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raven.api.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Optional<Post> findByWebId(String webId);

}
