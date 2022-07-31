package com.raven.api.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.raven.api.model.Tag;

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
    
    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 2200)
    private String description;

    @NotNull
    private MultipartFile file;

    @NotNull
    private String tags;

    @NotNull
    private boolean mature;

}
