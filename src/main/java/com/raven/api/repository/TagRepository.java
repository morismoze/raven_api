package com.raven.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raven.api.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
