package com.raven.api.response;

import java.sql.Timestamp;
import java.util.List;

import com.raven.api.model.PostComment;
import com.raven.api.model.PostUpvote;
import com.raven.api.model.PostVote;
import com.raven.api.model.Tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostResponseDto {

    private String webId;

    private String title;

    private String description;

    private boolean mature;

    private String coverUrl;

    private String username;

    private List<Tag> tags;

    private Integer postUpvotes;

    private Integer postDownvotes;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
