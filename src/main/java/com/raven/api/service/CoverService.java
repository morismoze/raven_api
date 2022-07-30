package com.raven.api.service;

import com.raven.api.model.Cover;
import com.raven.api.model.Post;

public interface CoverService {
    
    Cover createCover(Post post, String coverUrl);

}
