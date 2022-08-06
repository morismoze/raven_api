package com.raven.api.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.raven.api.model.Post;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    
    Optional<Post> findByWebId(String webId);

}
