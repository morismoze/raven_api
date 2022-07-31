package com.raven.api.validation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.raven.api.request.PostRequestFileDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostRequestFileDtoValidator implements Validator {

    private static final String TITLE = "title";

    private static final String DESCRIPTION = "description";

    private static final String BYTES = "fileBytes";

    private static final String TAGS = "tags";

    private static final String MATURE = "mature";

    private final MessageSourceAccessor accessor;

    @Override
    public boolean supports(Class<?> aClass) {
        return PostRequestFileDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, TITLE, accessor.getMessage("post.title.empty"));
        ValidationUtils.rejectIfEmpty(errors, DESCRIPTION, accessor.getMessage("post.description.empty"));
        //ValidationUtils.rejectIfEmpty(errors, BYTES, accessor.getMessage("post.cover.file.empty"));
        ValidationUtils.rejectIfEmpty(errors, TAGS, accessor.getMessage("post.tags.empty"));
        ValidationUtils.rejectIfEmpty(errors, MATURE, accessor.getMessage("post.mature.empty"));
        
        final PostRequestFileDto postRequestFileDto = (PostRequestFileDto) target;

        /* if (!CoverUtils.isImage(postRequestFileDto.getFileBytes())) {
            errors.rejectValue(BYTES, accessor.getMessage("post.cover.notValid"));
        } */
    }
    
}
