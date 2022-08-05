package com.raven.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.raven.api.model.PostComment;

public interface PostCommentRepository extends PagingAndSortingRepository<PostComment, Long> {
    
    Page<PostComment> findAllByPostId(Long id, Pageable pageable);

    Optional<PostComment> findById(Long id);

    List<PostComment> findAllByIdAndUserId(Long id, Long userId);
    
}
