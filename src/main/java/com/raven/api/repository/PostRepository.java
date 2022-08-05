package com.raven.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.raven.api.model.Post;
import com.raven.api.model.PostComment;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    
    Optional<Post> findByWebId(String webId);

    @Query(value = "SELECT p.postComments FROM Post p WHERE p.webId = ?1")
    Page<PostComment> findCommentsByWebId(String webid, Pageable pageable);

}
