package com.raven.api.service;

import org.springframework.data.domain.Page;

import com.raven.api.model.Post;
import com.raven.api.model.PostComment;
import com.raven.api.model.User;

public interface PostCommentService {
    
    PostComment createPostComment(Post post, User user, String comment);

    Integer upvotePostComment(Long id, User user);

    Integer downvotePostComment(Long id, User user);

    Page<PostComment> getPageablePostComments(Post post, Integer page, Integer limit);

}
