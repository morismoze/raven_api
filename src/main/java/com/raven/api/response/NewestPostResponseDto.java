package com.raven.api.response;

import java.sql.Timestamp;

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
public class NewestPostResponseDto {
    
    private String webId;

    private String title;

    private boolean mature;

    private String coverUrl;

    private String coverBlurHash;

    private int coverWidth;

    private int coverHeight;

    private Timestamp createdAt;

    private Timestamp updatedAt;
    
}
