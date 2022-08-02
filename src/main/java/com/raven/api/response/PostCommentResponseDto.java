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

    private String username;

    private List<PostCommentUpvoteResponseDto> postCommentUpvotes;

    private List<PostCommentDownvoteResponseDto> postCommentDownvotes;

    private Timestamp createdAt;

    private Timestamp updatedAt;
    
}
