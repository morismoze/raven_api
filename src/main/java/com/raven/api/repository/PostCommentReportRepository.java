package com.raven.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raven.api.model.PostCommentReport;

@Repository
public interface PostCommentReportRepository extends JpaRepository<PostCommentReport, Long> {

}
