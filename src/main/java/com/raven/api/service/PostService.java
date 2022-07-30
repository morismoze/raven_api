package com.raven.api.service;

import com.raven.api.model.Post;
import com.raven.api.model.User;

public interface PostService {
    
    String createPostByCoverUrl(User user, Post post, String coverUrl);

    String createPostByCoverFile(User user, Post post, String coverFileBytes);

}
