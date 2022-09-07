package com.raven.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.raven.api.model.PostCommentReportReason;
import com.raven.api.response.PostCommentReportReasonResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostCommentReportReasonMapper {

    List<PostCommentReportReasonResponseDto> tagsTagsResponseDtoMapper(List<PostCommentReportReason> postCommentReportReasons);
    
}
