package com.raven.api.service;

import com.raven.api.model.PostComment;
import com.raven.api.model.PostCommentReport;
import com.raven.api.model.PostCommentReportReason;
import com.raven.api.model.User;

public interface PostCommentReportService {

    PostCommentReport createPostCommentReport(PostComment postComment, User user, String description, PostCommentReportReason postCommentReportReason);
    
    PostCommentReport findById(Long id);

}
