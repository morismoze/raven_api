package com.raven.api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.raven.api.model.Post;
import com.raven.api.model.User;

public interface PostService {
    
    String createPostByCoverUrl(User user, Post post, String coverUrl);

    String createPostByCoverFile(User user, Post post, MultipartFile file);

    Page<Post> findPageablePosts(Integer page, Integer limit);

    Post findByWebId(String webId);

    List<Post> findTop20NewestPosts();

    Integer upvotePost(String webId, User user);

    Integer downvotePost(String webId, User user);

    Integer countPostsByTag(Long tagId);

}
