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
import com.raven.api.model.enums.TagName;
import com.raven.api.repository.PostRepository;
import com.raven.api.service.CoverService;
import com.raven.api.service.PostDownvoteService;
import com.raven.api.service.PostService;
import com.raven.api.service.PostUpvoteService;
import com.raven.api.util.FileUtils;
import com.raven.api.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostUpvoteService postUpvoteService;

    private final PostDownvoteService postDownvoteService;

    private final PostRepository postRepository;

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
    public Page<Post> findPageablePosts(Integer page, Integer limit) {
        final Sort sort = Sort.by("createdAt").descending();
        final Pageable pageable = PageRequest.of(page, limit, sort);
        
        return this.postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> findPageablePostsByTagName(String tagName, Integer page, Integer limit) {
        final Sort sort = Sort.by("createdAt").descending();
        final Pageable pageable = PageRequest.of(page, limit, sort);
        
        try {
            return this.postRepository.findAllByTags_TagName(TagName.valueOf(tagName), pageable);
        } catch (IllegalArgumentException e) {
            throw new EntryNotFoundException(this.accessor.getMessage("tagPosts.notFound", new Object[]{tagName}));
        }
    }

    @Override
    public List<Post> findTop20NewestPosts() {
        return this.postRepository.findTop20ByOrderByCreatedAtDesc();
    }

    @Override
    public Post findByWebId(final String webId) {
        final Optional<Post> postOptional = this.postRepository.findByWebId(webId);
        
        if (postOptional.isEmpty()) {
            throw new EntryNotFoundException(this.accessor.getMessage("post.notFound", new Object[]{webId}));
        }

        return postOptional.get();
    }


    @Override
    public Integer upvotePost(String webId, User user) {
        final Post post = this.findByWebId(webId);

        try {
            final PostUpvote postUpvote = this.postUpvoteService.findByPostIdAndUserId(post.getId(), user.getId());
            // user upvoted already upvoted post, so remove the upvote
            this.postUpvoteService.deleteById(postUpvote.getId());
            return post.getPostUpvotes().size() - post.getPostDownvotes().size();
        } catch (EntryNotFoundException entryNotFoundExceptionUpvote) {
            // user hasn't upvoted the post, so create a new one
            try {
                PostDownvote postDownvote = this.postDownvoteService.findByPostIdAndUserId(post.getId(), user.getId());
                // user prevously downvoted the post, so remove the downvote
                this.postDownvoteService.deleteById(postDownvote.getId());
            } catch (EntryNotFoundException entryNotFoundExceptionDownvote) {
                // user hasn't previously downvoted the post
            }

            // create new upvote
            this.postUpvoteService.createPostUpvote(post, user);
            return post.getPostUpvotes().size() - post.getPostDownvotes().size();
        }
    }

    @Override
    public Integer downvotePost(String webId, User user) {
        final Post post = this.findByWebId(webId);

        try {
            final PostDownvote postdownvote = this.postDownvoteService.findByPostIdAndUserId(post.getId(), user.getId());
            // user downvoted already downvoted post, so remove the downvote
            this.postDownvoteService.deleteById(postdownvote.getId());
            return post.getPostUpvotes().size() - post.getPostDownvotes().size();
        } catch (EntryNotFoundException entryNotFoundExceptionUpvote) {
            // user hasn't upvoted the post, so create a new one
            try {
                PostUpvote postUpvote = this.postUpvoteService.findByPostIdAndUserId(post.getId(), user.getId());
                // user prevously upvoted the post, so remove the upvote
                this.postUpvoteService.deleteById(postUpvote.getId());
            } catch (EntryNotFoundException entryNotFoundExceptionDownvote) {
                // user hasn't previously downvoted the post
            }

            // create new downvote
            this.postDownvoteService.createPostDownvote(post, user);
            return post.getPostUpvotes().size() - post.getPostDownvotes().size();
        }
    }

    @Override
    public Integer countPostsByTag(Long tagId) {
        return this.postRepository.countByTags_Id(tagId);
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
