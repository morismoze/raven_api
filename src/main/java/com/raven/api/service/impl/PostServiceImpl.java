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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.raven.api.model.PostView;
import com.raven.api.model.User;
import com.raven.api.repository.PostCommentRepository;
import com.raven.api.repository.PostRepository;
import com.raven.api.service.CoverService;
import com.raven.api.service.PostCommentService;
import com.raven.api.service.PostService;
import com.raven.api.util.FileUtils;
import com.raven.api.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostCommentRepository postCommentRepository;

    private final PostCommentService postCommentService;

    private final CoverService coverService;

    private final Cloudinary cloudinary;

    private final MessageSourceAccessor accessor;

    @Override
    @Transactional
    public String createPostByCoverUrl(final User user, final Post post, final String coverUrl) {
        final Cover cover = this.coverService.createCover(post, coverUrl);
        final Post initPost = this.initializePost(post, user, cover);
  
        this.postRepository.save(initPost);

        return initPost.getWebId();
    }

    @Override
    @Transactional
    public String createPostByCoverFile(final User user, final Post post, final MultipartFile multipartFile) {
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

    @Override
    public Post getPost(final String webId) {
        final Optional<Post> postOptional = this.postRepository.findByWebId(webId);
        
        if (postOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("post.notFound", new Object[]{webId}));
        }

        return postOptional.get();
    }

    @Override
    public Page<PostComment> getPageablePostComments(final String webId, final Integer page, final Integer limit) {
        final Post post = this.getPost(webId);
        final Page<PostComment> comments = this.postCommentService.getPageablePostComments(post, page, limit);

        return comments;
    }

    @Override
    @Transactional
    public void createPostComment(final String webId, final User user, final String comment) {
        final Optional<Post> postOptional = this.postRepository.findByWebId(webId);
        
        if (postOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("post.notFound", new Object[]{webId}));
        }

        final Post post = postOptional.get();
        final PostComment postComment = this.postCommentService.createPostComment(post, user, comment);
        post.getPostComments().add(postComment);
    }

    private String uploadToCloudinary(final MultipartFile multipartFile) throws IOException {
        final File file = FileUtils.multipartToFile(multipartFile);
        final Map<String, String> options = new HashMap<>();
        options.put("folder", "raven_api_post_covers");
        final Map<String, String> result = this.cloudinary.uploader().upload(file, options);

        return result.get("url");
    }

    private Post initializePost(final Post post, final User user, final Cover cover) {
        final List<PostUpvote> postUpvotes = new ArrayList<>();
        final List<PostDownvote> postDownvotes = new ArrayList<>();
        final List<PostComment> postComments = new ArrayList<>();
        final List<PostView> postViews = new ArrayList<>();
        final String webId = StringUtils.generateUniqueAlphaNumericString(12);

        post.setWebId(webId);
        post.setUser(user);
        post.setCover(cover);
        post.setPostViews(postViews);
        post.setPostUpvotes(postUpvotes);
        post.setPostDownvotes(postDownvotes);
        post.setPostComments(postComments);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return post;
    }
    
}
