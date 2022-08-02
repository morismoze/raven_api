package com.raven.api.controller;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.raven.api.mapper.PostMapper;
import com.raven.api.model.Post;
import com.raven.api.model.User;
import com.raven.api.request.PostRequestFileDto;
import com.raven.api.request.PostRequestUrlDto;
import com.raven.api.response.PostCommentsResponseDto;
import com.raven.api.response.PostResponseDto;
import com.raven.api.response.Response;
import com.raven.api.service.PostService;
import com.raven.api.service.UserService;
import com.raven.api.validation.PostRequestFileDtoValidator;
import com.raven.api.validation.PostRequestUrlDtoValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final UserService userService;

    private final PostRequestUrlDtoValidator postRequestUrlDtoValidator;

    private final PostRequestFileDtoValidator postRequestFileDtoValidator;

    private final PostMapper postMapper;

    @PostMapping("/url/create")
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
    public ResponseEntity<Response<?>> createPostByCoverFile(final @ModelAttribute PostRequestFileDto postRequestFileDto, 
        final BindingResult errors) {
            System.out.println(postRequestFileDto);
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

    @GetMapping("/{webId}")
    public ResponseEntity<Response<?>> getPost(@PathVariable String webId) {
        Post post = this.postService.getPost(webId);
        PostResponseDto postResponseDto = this.postMapper.postPostResponseDtoMapper(post);

        return ResponseEntity.ok().body(Response.build(postResponseDto));
    }

    @GetMapping("/{webId}/comments")
    public ResponseEntity<Response<?>> getPostComments(@PathVariable String webId) {
        Post post = this.postService.getPost(webId);
        PostCommentsResponseDto postCommentsResponseDto = this.postMapper.postPostCommentsResponseDtoMapper(post);

        return ResponseEntity.ok().body(Response.build(postCommentsResponseDto));
    }
    
}
