package com.raven.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.raven.api.model.PostComment;
import com.raven.api.response.PostCommentResponseDto;

@Mapper(componentModel = "spring", uses = { PostCommentUpvoteMapper.class, PostCommentDownvoteMapper.class }, 
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostCommentMapper {

    @Mapping(source = "user.username", target = "username")
    public PostCommentResponseDto postCommentPostCommentResponseDtoMapper(PostComment postComment);

   
}
