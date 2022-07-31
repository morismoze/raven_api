package com.raven.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.exception.ServerErrorException;
import com.raven.api.model.Cover;
import com.raven.api.model.Post;
import com.raven.api.model.PostComment;
import com.raven.api.model.PostDownvote;
import com.raven.api.model.PostUpvote;
import com.raven.api.model.User;
import com.raven.api.repository.PostRepository;
import com.raven.api.service.CoverService;
import com.raven.api.service.PostService;
import com.raven.api.util.FileUtils;
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
        final Post initPost = this.initializePost(post, user, cover);
  
        this.postRepository.save(initPost);

        return initPost.getWebId();
        
    }

    @Override
    @Transactional
    public String createPostByCoverFile(User user, Post post, MultipartFile multipartFile) {
        try {
            final String cloudinaryUrl = this.uploadToCloudinary(multipartFile);
            final Cover cover = this.coverService.createCover(post, cloudinaryUrl);
            final Post initPost = this.initializePost(post, user, cover);

            this.postRepository.save(initPost);

            return initPost.getWebId();
        } catch (IOException e) {
            throw new ServerErrorException(this.accessor.getMessage("server.error"));
        }
    }

    private String uploadToCloudinary(MultipartFile multipartFile) throws IOException {
        final File file = FileUtils.multipartToFile(multipartFile);
        final Map<String, String> options = new HashMap<>();
        options.put("folder", "raven_api_post_covers");
        final Map<String, String> result = this.cloudinary.uploader().upload(file, options);

        return result.get("url");
    }

    private Post initializePost(Post post, User user, Cover cover) {
        final List<PostUpvote> postUpvotes = new ArrayList<>();
        final List<PostDownvote> postDownvotes = new ArrayList<>();
        final List<PostComment> postComments = new ArrayList<>();
        final String webId = StringUtils.generateUniqueAlphaNumericString(12);

        post.setWebId(webId);
        post.setUser(user);
        post.setCover(cover);
        post.setPostUpvotes(postUpvotes);
        post.setPostDownvotes(postDownvotes);
        post.setPostComments(postComments);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return post;
    }

    @Override
    public Post getPost(String webId) {
        final Optional<Post> post = this.postRepository.findByWebId(webId);
        
        if (post.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("post.notFound"));
        }

        return post.get();
    }
    
}
