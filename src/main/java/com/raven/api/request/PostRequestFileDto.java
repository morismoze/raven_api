package com.raven.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

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
public class PostRequestFileDto {
    
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 2200)
    private String description;

    @NotNull
    private MultipartFile file;

    @NotBlank
    private String tags;

    @NotNull
    private Boolean mature;

}
