package com.raven.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
        User currentUser = this.userService.findCurrent();
        Post post = this.postMapper.postPostRequestUrlDtoMapper(postRequestUrlDto);
        String id = this.postService.createPostByCoverUrl(currentUser, post, postRequestUrlDto.getUrl());

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(Response.build(errors));
        }

        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/url/create").toUriString());
        return ResponseEntity.created(uri).body(Response.build(id));
        
    }

    @PostMapping("/file/create")
    public ResponseEntity<Response<?>> createPostByCoverFile(final @RequestBody PostRequestFileDto postRequestFileDto, 
        final BindingResult errors) {
            this.postRequestFileDtoValidator.validate(postRequestFileDto, errors);
            User currentUser = this.userService.findCurrent();
            Post post = this.postMapper.postPostRequestFileDtoMapper(postRequestFileDto);
            String id = this.postService.createPostByCoverFile(currentUser, post, postRequestFileDto.getFileBytes());
    
            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().body(Response.build(errors));
            }
    
            final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/post/file/create").toUriString());
            return ResponseEntity.created(uri).body(Response.build(id));
    }
    
}
