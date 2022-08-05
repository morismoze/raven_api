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
import com.raven.api.model.PostComment;
import com.raven.api.model.PostDownvote;
import com.raven.api.model.PostUpvote;
import com.raven.api.model.PostView;
import com.raven.api.model.Tag;
import com.raven.api.request.PostRequestFileDto;
import com.raven.api.request.PostRequestUrlDto;
import com.raven.api.response.PostCommentsResponseDto;
import com.raven.api.response.PostResponseDto;

@Mapper(componentModel = "spring", uses = { PostCommentMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    Post postPostRequestUrlDtoMapper(PostRequestUrlDto postRequestUrlDto);

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

    @Mapping(target = "tags", qualifiedByName = "tagsMapper")
    Post postPostRequestFileDtoMapper(PostRequestFileDto postRequestFileDto);

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

    @Named("postViewsMapper")
    default Integer postViewsMapper(List<PostView> postViews) {
        return postViews.size();
    }

    @Mapping(source = "cover.url", target = "coverUrl")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "postUpvotes", target = "upvotes", qualifiedByName = "postUpvotesMapper")
    @Mapping(source = "postDownvotes", target = "downvotes", qualifiedByName = "postDownvotesMapper")
    @Mapping(source = ".", target = "votes", qualifiedByName = "postVotesMapper")
    @Mapping(source = "postViews", target = "views", qualifiedByName = "postViewsMapper")
    PostResponseDto postPostResponseDtoMapper(Post post);

    @Mapping(source = "postComments", target = "comments")
    PostCommentsResponseDto postCommentsPostCommentsResponseDtoMapper(Long count, Integer nextPage, List<PostComment> postComments);
    
}
