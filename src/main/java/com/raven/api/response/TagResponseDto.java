package com.raven.api.response;

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
public class TagResponseDto {
    
    private Long id;

    private String tagName;

    private String tagDisplayName;

    private Integer posts;

}
