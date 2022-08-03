package com.raven.api.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.raven.api.model.Post;
import com.raven.api.model.PostComment;
import com.raven.api.model.User;

public interface PostService {
    
    String createPostByCoverUrl(User user, Post post, String coverUrl);

    String createPostByCoverFile(User user, Post post, MultipartFile file);

    Post getPost(String webId);

    List<PostComment> getPageablePostComments(String webId, Integer page, Integer limit);

    void createPostComment(String webId, User user, String comment);

}
