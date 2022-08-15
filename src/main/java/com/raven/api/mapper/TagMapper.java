package com.raven.api.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.raven.api.model.Tag;
import com.raven.api.response.TagResponseDto;
import com.raven.api.service.PostService;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Named("tagDisplayNameMapper")
    default String tagDisplayNameMapper(Tag tag) {
        return tag.getTagName().name().replace("_", " ");
    }

    @Mapping(source = ".", target = "tagDisplayName", qualifiedByName = "tagDisplayNameMapper")
    @Named("tagsPostTagsMapper")
    TagResponseDto tagPostTagResponseDtoMapper(Tag tag);

    @Mapping(target = "posts", ignore = true)
    @IterableMapping(qualifiedByName = "tagsPostTagsMapper")
    List<TagResponseDto> tagsPostTagsResponseDtoMapper(List<Tag> tags);

    @AfterMapping
    default void tagTagResponseDtoMapper( @MappingTarget TagResponseDto target, Tag tag, @Context PostService postService) {
        target.setPosts(postService.countPostsByTag(tag.getId()));
    }

    @Mapping(source = ".", target = "tagDisplayName", qualifiedByName = "tagDisplayNameMapper")
    @Named("tagsTagsMapper")
    TagResponseDto tagTagResponseDtoMapper(Tag tag, @Context PostService postService);

    @IterableMapping(qualifiedByName = "tagsTagsMapper")
    List<TagResponseDto> tagsTagsResponseDtoMapper(List<Tag> tags, @Context PostService postService);
    
}
