package com.raven.api.mapper;

import java.util.Optional;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.raven.api.model.PostComment;
import com.raven.api.model.PostCommentDownvote;
import com.raven.api.model.PostCommentUpvote;
import com.raven.api.response.PostCommentResponseDto;
import com.raven.api.security.jwt.AuthUtils;

@Mapper(componentModel = "spring", uses = { PostCommentUpvoteMapper.class, PostCommentDownvoteMapper.class }, 
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostCommentMapper {

    @Named("postCommentUpvotesQuantityMapper")
    default Integer postCommentUpvotesQuantityMapper(Set<PostCommentUpvote> postCommentUpvotes) {
        return postCommentUpvotes.size();
    }

    @Named("postCommentDownvotesQuantityMapper")
    default Integer postCommentDownvotesQuantityMapper(Set<PostCommentDownvote> postCommentDownvotes) {
        return postCommentDownvotes.size();
    }

    @Named("postCommentVotesQuantityMapper")
    default Integer postCommentVotesQuantityMapper(PostComment postComment) {
        return postComment.getPostCommentUpvotes().size() - postComment.getPostCommentDownvotes().size();
    }

    @Named("postCommentUpvotedByUserPrincipalMapper")
    default boolean postCommentUpvotedByUserPrincipalMapper(Set<PostCommentUpvote> postCommentUpvotes) {
        Optional<String> userPrincipalOptional = AuthUtils.getCurrentUserUsername();

        if (userPrincipalOptional.isEmpty()) {
            return false;
        }

        String userPrincipal = userPrincipalOptional.get();

        for (PostCommentUpvote postCommentUpvote : postCommentUpvotes) {
            if (postCommentUpvote.getUser().getUsername().equals(userPrincipal)) {
                return true;
            }
        }

        return false;
    }

    @Named("postCommentDownvotedByUserPrincipalMapper")
    default boolean postCommentDownvotedByUserPrincipalMapper(Set<PostCommentDownvote> postCommentDownvotes) {
        Optional<String> userPrincipalOptional = AuthUtils.getCurrentUserUsername();

        if (userPrincipalOptional.isEmpty()) {
            return false;
        }

        String userPrincipal = userPrincipalOptional.get();

        for (PostCommentDownvote postCommentDownvote : postCommentDownvotes) {
            if (postCommentDownvote.getUser().getUsername().equals(userPrincipal)) {
                return true;
            }
        }

        return false;
    }

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "postCommentUpvotes", target = "upvotes", qualifiedByName = "postCommentUpvotesQuantityMapper")
    @Mapping(source = "postCommentDownvotes", target = "downvotes", qualifiedByName = "postCommentDownvotesQuantityMapper")
    @Mapping(source = ".", target = "votes", qualifiedByName = "postCommentVotesQuantityMapper")
    @Mapping(source = "postCommentUpvotes", target = "userPrincipalUpvoted", qualifiedByName = "postCommentUpvotedByUserPrincipalMapper")
    @Mapping(source = "postCommentDownvotes", target = "userPrincipalDownvoted", qualifiedByName = "postCommentDownvotedByUserPrincipalMapper")
    public PostCommentResponseDto postCommentPostCommentResponseDtoMapper(PostComment postComment);

}
