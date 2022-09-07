package com.raven.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raven.api.mapper.PostCommentReportReasonMapper;
import com.raven.api.model.PostCommentReportReason;
import com.raven.api.response.PostCommentReportReasonResponseDto;
import com.raven.api.response.Response;
import com.raven.api.service.PostCommentReportReasonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post-comment-report-reason")
@RequiredArgsConstructor
public class PostCommentReportReasonController {

    private final PostCommentReportReasonService postCommentReportReasonService;

    private final PostCommentReportReasonMapper postCommentReportReasonMapper;

    @GetMapping("/all")
    public ResponseEntity<Response<?>> getAllTags() {
        final List<PostCommentReportReason> postCommentReportReasons = this.postCommentReportReasonService.findAll();

        final List<PostCommentReportReasonResponseDto> postCommentReportReasonsResponseDto = this.postCommentReportReasonMapper.tagsTagsResponseDtoMapper(postCommentReportReasons);

        return ResponseEntity.ok().body(Response.build(postCommentReportReasonsResponseDto));
    }
    
}
