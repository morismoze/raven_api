package com.raven.api.service.impl;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.raven.api.model.Cover;
import com.raven.api.model.Post;
import com.raven.api.repository.CoverRepository;
import com.raven.api.service.CoverService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoverServiceImpl implements CoverService {

    private final CoverRepository coverRepository;
    
    @Override
    @Transactional
    public Cover createCover(final Post post, final String coverUrl) {
        final Cover cover = new Cover();
        
        cover.setPost(post);
        cover.setUrl(coverUrl);
        cover.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        cover.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return this.coverRepository.save(cover);
    }

}
