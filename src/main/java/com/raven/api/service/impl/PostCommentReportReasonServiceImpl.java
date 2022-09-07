package com.raven.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raven.api.model.PostCommentReportReason;
import com.raven.api.repository.PostCommentReportReasonRepository;
import com.raven.api.service.PostCommentReportReasonService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommentReportReasonServiceImpl implements PostCommentReportReasonService {

    private final PostCommentReportReasonRepository postCommentReportReasonRepository;

    @Override
    public List<PostCommentReportReason> findAll() {
        return this.postCommentReportReasonRepository.findAll();
    }
    
}
