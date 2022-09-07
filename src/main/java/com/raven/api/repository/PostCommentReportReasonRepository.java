package com.raven.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raven.api.model.PostCommentReportReason;

@Repository
public interface PostCommentReportReasonRepository extends JpaRepository<PostCommentReportReason, Long> {

}
