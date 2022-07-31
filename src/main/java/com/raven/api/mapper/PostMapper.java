package com.raven.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
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

    @Mapping(target = "tags", qualifiedByName = "tagsMapper")
    Post postPostRequestFileDtoMapper(PostRequestFileDto postRequestFileDto);

    @Named("tagsMapper")
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

    @Named("postUpvotesMapper")
    default Integer postUpvotesMapper(List<PostUpvote> postUpvotes) {
        return postUpvotes.size();
    }

    @Named("postDownvotesMapper")
    default Integer postDownvotesMapper(List<PostDownvote> postDownvotes) {
        return postDownvotes.size();
    }

    @Named("postVotesMapper")
    default Integer postVotesMapper(Post post) {
        return post.getPostUpvotes().size() - post.getPostDownvotes().size();
    }

    @Mapping(source = "cover.url", target = "coverUrl")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "postUpvotes", target = "upvotes", qualifiedByName = "postUpvotesMapper")
    @Mapping(source = "postDownvotes", target = "downvotes", qualifiedByName = "postDownvotesMapper")
    @Mapping(target = "votes", source = ".", qualifiedByName = "postVotesMapper")
    PostResponseDto postPostResponseDtoMapper(Post post);
    
}
