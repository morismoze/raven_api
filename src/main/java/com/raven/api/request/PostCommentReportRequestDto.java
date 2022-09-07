package com.raven.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.raven.api.model.PostCommentReportReason;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostCommentReportRequestDto {

    @NotBlank
    @Size(max = 2200)
    private String description;

    @NotBlank
    private PostCommentReportReason reason;
    
}
