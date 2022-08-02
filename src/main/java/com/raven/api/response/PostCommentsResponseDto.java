package com.raven.api.response;

import java.util.List;

import com.raven.api.model.PostComment;

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
public class PostCommentsResponseDto {
    
    private List<PostCommentResponseDto> comments;

    private Integer count;

}