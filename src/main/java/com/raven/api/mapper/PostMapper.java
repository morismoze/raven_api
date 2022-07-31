package com.raven.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.raven.api.exception.ServerErrorException;
import com.raven.api.model.Post;
import com.raven.api.model.PostDownvote;
import com.raven.api.model.PostUpvote;
import com.raven.api.model.Tag;
import com.raven.api.request.PostRequestFileDto;
import com.raven.api.request.PostRequestUrlDto;
import com.raven.api.response.PostResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    Post postPostRequestUrlDtoMapper(PostRequestUrlDto postRequestUrlDto);

    @Mapping(target = "tags")
    Post postPostRequestFileDtoMapper(PostRequestFileDto postRequestFileDto);

    default List<Tag> tagsMapper(String tagsString) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        try {
            List<Tag> tags = objectMapper.readValue(tagsString, typeFactory.constructCollectionType(List.class, Tag.class));
            return tags;
        } catch (JsonProcessingException e) {
            throw new ServerErrorException("The application has encountered an unexpected error");
        }
    }

    @Mapping(source = "cover.url", target = "coverUrl")
    @Mapping(source = "user.username", target = "username")
    PostResponseDto postPostResponseDtoMapper(Post post);
    
    default Integer postUpvotesMapper(List<PostUpvote> postUpvotes) {
        return postUpvotes.size();
    }

    default Integer postDownvotesMapper(List<PostDownvote> postDownvotes) {
        return postDownvotes.size();
    }
    
}
