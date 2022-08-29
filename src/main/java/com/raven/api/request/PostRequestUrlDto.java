package com.raven.api.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class PostRequestUrlDto {
    
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 2200)
    private String description;

    @NotBlank
    private String url;

    @NotEmpty
    private List<Tag> tags;

    @NotNull
    private Boolean mature;

}
