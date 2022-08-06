package com.raven.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raven.api.model.Tag;
import com.raven.api.response.Response;
import com.raven.api.service.TagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/all")
    public ResponseEntity<Response<?>> getAllTags() {
            final List<Tag> tags = this.tagService.findAll();

            return ResponseEntity.ok().body(Response.build(tags));
    }
    
}
