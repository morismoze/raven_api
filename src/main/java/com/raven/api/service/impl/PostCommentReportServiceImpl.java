package com.raven.api.service.impl;

import java.util.Optional;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.model.PostComment;
import com.raven.api.model.PostCommentReport;
import com.raven.api.model.PostCommentReportReason;
import com.raven.api.model.User;
import com.raven.api.repository.PostCommentReportRepository;
import com.raven.api.service.PostCommentReportService;

import java.sql.Timestamp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommentReportServiceImpl implements PostCommentReportService {

    private final PostCommentReportRepository postCommentReportRepository;

    private final MessageSourceAccessor accessor;

    @Override
    public PostCommentReport createPostCommentReport(PostComment postComment, User user, String description,
        PostCommentReportReason postCommentReportReason) {
        final PostCommentReport postCommentReport = new PostCommentReport();

        postCommentReport.setUser(user);
        postCommentReport.setPostComment(postComment);
        postCommentReport.setDescription(description);
        postCommentReport.setReason(postCommentReportReason);
        postCommentReport.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        postCommentReport.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return this.postCommentReportRepository.save(postCommentReport);
    }

    @Override
    public PostCommentReport findById(Long id) {
        final Optional<PostCommentReport> postCommentReportOptional = this.postCommentReportRepository.findById(id);

        if (postCommentReportOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("postCommentReport.notFound", new Object[]{id}));
        }

        return postCommentReportOptional.get();
    }

}
