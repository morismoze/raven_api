package com.raven.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raven.api.mapper.TagMapper;
import com.raven.api.model.Tag;
import com.raven.api.response.Response;
import com.raven.api.response.TagResponseDto;
import com.raven.api.service.PostService;
import com.raven.api.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    private final PostService postService;

    private final TagMapper tagMapper;

    @GetMapping("/all")
    public ResponseEntity<Response<?>> getAllTags() {
            final List<Tag> tags = this.tagService.findAll();
            final List<TagResponseDto> tagsResponseDto = this.tagMapper.tagsTagsResponseDtoMapper(tags, this.postService);

            return ResponseEntity.ok().body(Response.build(tagsResponseDto));
    }
    
}
