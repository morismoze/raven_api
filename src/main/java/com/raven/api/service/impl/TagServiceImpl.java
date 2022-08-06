package com.raven.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raven.api.model.Tag;
import com.raven.api.repository.TagRepository;
import com.raven.api.service.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> findAll() {
        return this.tagRepository.findAll();
    }
    
}
