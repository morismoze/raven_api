package com.raven.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.raven.api.model.PostCommentUpvote;
import com.raven.api.response.PostCommentUpvoteResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostCommentUpvoteMapper {

    PostCommentUpvoteResponseDto postcommentUpvotePostCommentUpvoteResponseDto(PostCommentUpvote postCommentUpvote);

}
