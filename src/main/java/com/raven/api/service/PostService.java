package com.raven.api.service;

import org.springframework.web.multipart.MultipartFile;

import com.raven.api.model.Post;
import com.raven.api.model.User;

public interface PostService {
    
    String createPostByCoverUrl(User user, Post post, String coverUrl);

    String createPostByCoverFile(User user, Post post, MultipartFile file);

    Post getPost(String webId);

}
