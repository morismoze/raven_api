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
public class PostCommentResponseDto {

    private Long id;

    private String comment;

    private String userId;

    private String username;

    private Integer upvotes;

    private Integer downvotes;

    private Integer votes;

    private Boolean userPrincipalUpvoted;

    private Boolean userPrincipalDownvoted;

    private Timestamp createdAt;

    private Timestamp updatedAt;
    
}
