package com.raven.api.mapper;

import org.mapstruct.Mapper;

import com.raven.api.model.Post;
import com.raven.api.request.PostRequestFileDto;
import com.raven.api.request.PostRequestUrlDto;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post postPostRequestUrlDtoMapper(PostRequestUrlDto postRequestUrlDto);

    Post postPostRequestFileDtoMapper(PostRequestFileDto postRequestFileDto);
    
}
