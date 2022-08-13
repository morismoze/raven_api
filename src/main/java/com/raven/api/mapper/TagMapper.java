package com.raven.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.raven.api.model.Tag;
import com.raven.api.response.TagResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Named("tagNameMapper")
    default String tagNameMapper(Tag tag) {
        return tag.getTagName().name().replace("_", " ");
    }

    @Named("tagPostsQuantityMapper")
    default Integer tagPostsQuantityMapper(Tag tag) {
        return 1;
    }

    @Mapping(source = ".", target = "tagName", qualifiedByName = "tagNameMapper")
    @Mapping(source = ".", target = "posts", qualifiedByName = "tagPostsQuantityMapper")
    TagResponseDto tagTagResponseDtoMapper(Tag tag);

    List<TagResponseDto> tagsTagsResponseDtoMapper(List<Tag> tags);
    
}
