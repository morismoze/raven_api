package com.raven.api.service;

import com.raven.api.model.Post;
import com.raven.api.model.PostComment;
import com.raven.api.model.User;

public interface PostCommentService {
    
    PostComment createPostComment(Post post, User user, String comment);

}
