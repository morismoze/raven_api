package com.raven.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.raven.api.model.PostCommentDownvote;
import com.raven.api.response.PostCommentDownvoteResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostCommentDownvoteMapper {

    PostCommentDownvoteResponseDto postCommentDownvotePostCommentUpvoteResponseDto(PostCommentDownvote postCommentDownvote);

}
