package com.raven.api.response;

import java.sql.Timestamp;
import java.util.List;

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

    private String coverBlurHash;

    private int coverWidth;

    private int coverHeight;

    private Long userId;

    private String username;

    private Boolean userPrincipalUpvoted;

    private Boolean userPrincipalDownvoted;

    private List<TagResponseDto> tags;

    private Integer upvotes;

    private Integer downvotes;

    private Integer votes;

    private Integer views;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
