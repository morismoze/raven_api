package com.raven.api.mapper;

import java.util.List;
import java.util.Optional;

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
import com.raven.api.response.PostResponseDto;
import com.raven.api.response.PostsResponseDto;
import com.raven.api.response.ReducedPostResponseDto;
import com.raven.api.security.jwt.AuthUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Named("postVotesMapper")
    default Integer postVotesMapper(Post post) {
        return post.getPostUpvotes().size() - post.getPostDownvotes().size();
    }

    @Named("postCommentsQuantityMapper")
    default Integer postCommentsQuantityMapper(List<PostComment> postComments) {
        return postComments.size();
    }

    @Mapping(source = "cover.url", target = "coverUrl")
    @Mapping(source = ".", target = "votes", qualifiedByName = "postVotesMapper")
    @Mapping(source = "postComments", target = "comments", qualifiedByName = "postCommentsQuantityMapper")
    @Mapping(source = "postViews", target = "views", qualifiedByName = "postViewsMapper")
    ReducedPostResponseDto postReducedPostResponseDto(Post post);

    PostsResponseDto postsPostsResponseDtoMapper(Long count, Integer nextPage, List<Post> posts);

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

    @Named("postUpvotedByUserPrincipalMapper")
    default boolean postUpvotedByUserPrincipalMapper(List<PostUpvote> postUpvotes) {
        Optional<String> userPrincipalOptional = AuthUtils.getCurrentUserUsername();

        if (userPrincipalOptional.isEmpty()) {
            return false;
        }

        String userPrincipal = userPrincipalOptional.get();
        for (PostUpvote postUpvote : postUpvotes) {
            if (postUpvote.getUser().getUsername().equals(userPrincipal)) {
                System.out.println("NASO");
                return true;
            }
        }

        return false;
    }

    @Named("postDownvotedByUserPrincipalMapper")
    default boolean postDownvotedByUserPrincipalMapper(List<PostDownvote> postDownvotes) {
        Optional<String> userPrincipalOptional = AuthUtils.getCurrentUserUsername();

        if (userPrincipalOptional.isEmpty()) {
            return false;
        }

        String userPrincipal = userPrincipalOptional.get();
        for (PostDownvote postDownvote : postDownvotes) {
            if (postDownvote.getUser().getUsername().equals(userPrincipal)) {
                return true;
            }
        }

        return false;
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

    @Named("postViewsMapper")
    default Integer postViewsMapper(List<PostView> postViews) {
        return postViews.size();
    }

    @Mapping(source = "cover.url", target = "coverUrl")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "postUpvotes", target = "userPrincipalUpvoted", qualifiedByName = "postUpvotedByUserPrincipalMapper")
    @Mapping(source = "postDownvotes", target = "userPrincipalDownvoted", qualifiedByName = "postDownvotedByUserPrincipalMapper")
    @Mapping(source = "postUpvotes", target = "upvotes", qualifiedByName = "postUpvotesMapper")
    @Mapping(source = "postDownvotes", target = "downvotes", qualifiedByName = "postDownvotesMapper")
    @Mapping(source = ".", target = "votes", qualifiedByName = "postVotesMapper")
    @Mapping(source = "postViews", target = "views", qualifiedByName = "postViewsMapper")
    PostResponseDto postPostResponseDtoMapper(Post post);
    
}
