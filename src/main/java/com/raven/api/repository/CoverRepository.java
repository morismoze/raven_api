package com.raven.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raven.api.model.Cover;

@Repository
public interface CoverRepository extends JpaRepository<Cover, Long>{
    
}
