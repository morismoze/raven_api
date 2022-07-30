package com.raven.api.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.raven.api.exception.ServerErrorException;
import com.raven.api.model.Cover;
import com.raven.api.model.Post;
import com.raven.api.model.PostComment;
import com.raven.api.model.PostVote;
import com.raven.api.model.User;
import com.raven.api.repository.PostRepository;
import com.raven.api.service.CoverService;
import com.raven.api.service.PostService;
import com.raven.api.util.CoverUtils;
import com.raven.api.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final CoverService coverService;

    private final Cloudinary cloudinary;

    private final MessageSourceAccessor accessor;

    @Override
    @Transactional
    public String createPostByCoverUrl(User user, Post post, String coverUrl) {
        final Cover cover = this.coverService.createCover(post, coverUrl);
        Post initPost = this.initializePost(post, user, cover);
  
        this.postRepository.save(initPost);

        return StringUtils.generateUniqueAlphaNumericString(8);
        
    }

    @Override
    @Transactional
    public String createPostByCoverFile(User user, Post post, String coverStringBytes) {
        try {
            final String cloudinaryUrl = this.uploadToCloudinary(coverStringBytes);
            final Cover cover = this.coverService.createCover(post, cloudinaryUrl);
            Post initPost = this.initializePost(post, user, cover);

            this.postRepository.save(initPost);

            return StringUtils.generateUniqueAlphaNumericString(8);
        } catch (IOException e) {
            throw new ServerErrorException(this.accessor.getMessage("server.error"));
        }
    }

    private String uploadToCloudinary(String coverStringBytes) throws IOException {
        Map<String, String> options = new HashMap<>();
        Map<String, String> result = this.cloudinary.uploader().upload(coverStringBytes, options);

        return result.get("url");
    }

    private Post initializePost(Post post, User user, Cover cover) {
        final List<PostVote> postVotes = new ArrayList<>();
        final List<PostComment> postComments = new ArrayList<>();
        post.setUser(user);
        post.setCover(cover);
        post.setPostVotes(postVotes);
        post.setPostComments(postComments);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return post;
    }
    
}
