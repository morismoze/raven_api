package com.raven.api.controller;

import java.net.URI;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.raven.api.mapper.PostCommentMapper;
import com.raven.api.mapper.PostMapper;
import com.raven.api.model.Post;
import com.raven.api.model.PostComment;
import com.raven.api.model.User;
import com.raven.api.request.PostCommentReportRequestDto;
import com.raven.api.request.PostCommentRequestDto;
import com.raven.api.request.PostRequestFileDto;
import com.raven.api.request.PostRequestUrlDto;
import com.raven.api.response.NewestPostResponseDto;
import com.raven.api.response.PostCommentsResponseDto;
import com.raven.api.response.PostResponseDto;
import com.raven.api.response.PostsResponseDto;
import com.raven.api.response.Response;
import com.raven.api.service.PostCommentService;
import com.raven.api.service.PostService;
import com.raven.api.service.UserService;
import com.raven.api.validation.PostCommentReportReasonValidator;
import com.raven.api.validation.PostCommentRequestDtoValidator;
import com.raven.api.validation.PostRequestFileDtoValidator;
import com.raven.api.validation.PostRequestUrlDtoValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final UserService userService;

    private final PostCommentService postCommentService;

    private final PostRequestUrlDtoValidator postRequestUrlDtoValidator;

    private final PostRequestFileDtoValidator postRequestFileDtoValidator;

    private final PostCommentRequestDtoValidator postCommentRequestDtoValidator;

    private final PostCommentReportReasonValidator postCommentReportReasonValidator;

    private final PostMapper postMapper;

    private final PostCommentMapper postCommentMapper;

    @PostMapping("/url/create")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> createPostByCoverUrl(final @RequestBody PostRequestUrlDto postRequestUrlDto, 
        final BindingResult errors) {
        this.postRequestUrlDtoValidator.validate(postRequestUrlDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(Response.build(errors));
        }

        final User currentUser = this.userService.findCurrent();
        final Post post = this.postMapper.postPostRequestUrlDtoMapper(postRequestUrlDto);
        final String webId = this.postService.createPostByCoverUrl(currentUser, post, postRequestUrlDto.getUrl());
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/url/create").toUriString());
        
        return ResponseEntity.created(uri).body(Response.build(webId));
    }

    @PostMapping(path = "/file/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> createPostByCoverFile(final @ModelAttribute PostRequestFileDto postRequestFileDto, 
        final BindingResult errors) {
        this.postRequestFileDtoValidator.validate(postRequestFileDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(Response.build(errors));
        }

        final User currentUser = this.userService.findCurrent();
        final Post post = this.postMapper.postPostRequestFileDtoMapper(postRequestFileDto);
        final String webId = this.postService.createPostByCoverFile(currentUser, post, postRequestFileDto.getFile());
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/file/create").toUriString());
        return ResponseEntity.created(uri).body(Response.build(webId));
    }

    @GetMapping(value = "/all", params = { "page", "limit" })
    public ResponseEntity<Response<?>> getAllPosts(final @RequestParam @NotNull Integer page, 
        final @RequestParam @NotNull Integer limit) {
        final Page<Post> posts = this.postService.findPageablePosts(page, limit);
        final Integer nextPage = posts.hasNext() ? posts.getPageable().getPageNumber() + 1 : null;
        final PostsResponseDto postResponseDto = this.postMapper.postsPostsResponseDtoMapper(posts.getTotalElements(),
            nextPage, posts.getContent());

        return ResponseEntity.ok().body(Response.build(postResponseDto));
    }

    @GetMapping(value = "/all", params = { "tagName", "page", "limit" })
    public ResponseEntity<Response<?>> findAllPostsByTagName(final @RequestParam @NotBlank String tagName, 
        final @RequestParam @NotNull Integer page, final @RequestParam @NotNull Integer limit) {
        final Page<Post> posts = this.postService.findPageablePostsByTagName(tagName, page, limit);
        final Integer nextPage = posts.hasNext() ? posts.getPageable().getPageNumber() + 1 : null;
        final PostsResponseDto postResponseDto = this.postMapper.postsPostsResponseDtoMapper(posts.getTotalElements(),
            nextPage, posts.getContent());

        return ResponseEntity.ok().body(Response.build(postResponseDto));
    }

    @GetMapping("/newest")
    public ResponseEntity<Response<?>> getNewestTop20Posts() {
        final List<Post> posts = this.postService.findTop20NewestPosts();
        final List<NewestPostResponseDto> postsReponseDto = this.postMapper.postsNewestPostsResponseDtoMapper(posts);

        return ResponseEntity.ok().body(Response.build(postsReponseDto));
    }

    @GetMapping("/{webId}")
    public ResponseEntity<Response<?>> getPost(final @PathVariable @NotBlank String webId) {
        final Post post = this.postService.findByWebId(webId);
        final PostResponseDto postResponseDto = this.postMapper.postPostResponseDtoMapper(post);

        return ResponseEntity.ok().body(Response.build(postResponseDto));
    }


    @PostMapping("/{webId}/upvote")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> upvotePost(final @PathVariable @NotBlank String webId) {
        final User currentUser = this.userService.findCurrent();
        final Integer votes = this.postService.upvotePost(webId, currentUser);
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/" + webId + "/upvote").toUriString());
        
        return ResponseEntity.created(uri).body(Response.build(votes));
    }

    @PostMapping("/{webId}/downvote")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> downvotePost(final @PathVariable @NotBlank String webId) {
        final User currentUser = this.userService.findCurrent();
        final Integer votes = this.postService.downvotePost(webId, currentUser);
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/" + webId + "/downvote").toUriString());
        
        return ResponseEntity.created(uri).body(Response.build(votes));
    }

    @GetMapping("/{webId}/comments")
    public ResponseEntity<Response<?>> getPostComments(final @PathVariable @NotBlank String webId, 
        final @RequestParam @NotNull Integer page, final @RequestParam @NotNull Integer limit) {
        final Post post = this.postService.findByWebId(webId);
        final Page<PostComment> postComments = this.postCommentService.findPageablePostComments(post, page, limit);
        final Integer nextPage = postComments.hasNext() ? postComments.getPageable().getPageNumber() + 1 : null;
        final PostCommentsResponseDto postCommentsResponseDto = this.postCommentMapper.postCommentsPostCommentsResponseDtoMapper(
            postComments.getTotalElements(), nextPage, postComments.getContent());

        return ResponseEntity.ok().body(Response.build(postCommentsResponseDto));
    }

    @PostMapping("/{webId}/comments/create")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> createPostComment(final @PathVariable @NotBlank String webId, 
        final @RequestBody PostCommentRequestDto postCommentRequestDto, final BindingResult errors) {
        this.postCommentRequestDtoValidator.validate(postCommentRequestDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(Response.build(errors));
        }

        final Post post = this.postService.findByWebId(webId);
        final User currentUser = this.userService.findCurrent();
        this.postCommentService.createPostComment(post, currentUser, postCommentRequestDto.getComment());
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/" + webId + "comments/create").toUriString());
        
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/{webId}/comments/{id}/upvote")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> upvotePostComment(final @PathVariable @NotBlank String webId, 
        final @PathVariable @NotNull Long id) {
        final User currentUser = this.userService.findCurrent();
        final Integer votes = this.postCommentService.upvotePostComment(id, currentUser);
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/" + webId + "comments/" + id + "/upvote").toUriString());
        
        return ResponseEntity.created(uri).body(Response.build(votes));
    }

    @PostMapping("/{webId}/comments/{id}/downvote")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> downvotePostComment(final @PathVariable @NotBlank String webId, 
        final @PathVariable @NotNull Long id) {
        final User currentUser = this.userService.findCurrent();
        final Integer votes = this.postCommentService.downvotePostComment(id, currentUser);
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/" + webId + "comments/" + id + "/downvote").toUriString());
        
        return ResponseEntity.created(uri).body(Response.build(votes));
    }

    @PostMapping("/{webId}/comments/{id}/report")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Response<?>> reportPostComment(final @PathVariable @NotBlank String webId, 
        final @PathVariable @NotNull Long id, @RequestBody PostCommentReportRequestDto postCommentReportRequestDto, final BindingResult errors) {    
        this.postCommentReportReasonValidator.validate(postCommentReportRequestDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(Response.build(errors));
        }
        
        final User currentUser = this.userService.findCurrent();
        this.postCommentService.reportPostComment(currentUser, id, postCommentReportRequestDto.getDescription(), postCommentReportRequestDto.getReason());
        
        return ResponseEntity.ok().body(Response.build("message", false));
    }
    
}
