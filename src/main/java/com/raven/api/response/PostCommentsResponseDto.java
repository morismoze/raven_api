package com.raven.api.response;

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
public class PostCommentsResponseDto {
    
    private List<PostCommentResponseDto> comments;

    private Long count;

    private Integer nextPage;

}
