package com.raven.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.raven.api.model.Post;
import com.raven.api.model.enums.TagName;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    
    Optional<Post> findByWebId(String webId);

    Integer countByTags_Id(Long postId);

    List<Post> findTop20ByOrderByCreatedAtDesc();

    Page<Post> findAllByTags_TagName(TagName tagName, Pageable pageable);

}
