package com.raven.api.validation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.raven.api.request.PostRequestUrlDto;
import com.raven.api.util.CoverUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostRequestUrlDtoValidator implements Validator {

    private static final String TITLE = "title";

    private static final String DESCRIPTION = "description";

    private static final String URL = "url";

    private static final String TAGS = "tags";

    private static final String MATURE = "mature";

    private final MessageSourceAccessor accessor;

    @Override
    public boolean supports(Class<?> aClass) {
        return PostRequestUrlDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, TITLE, accessor.getMessage("post.title.empty"));
        ValidationUtils.rejectIfEmpty(errors, DESCRIPTION, accessor.getMessage("post.description.empty"));
        ValidationUtils.rejectIfEmpty(errors, URL, accessor.getMessage("post.cover.url.empty"));
        ValidationUtils.rejectIfEmpty(errors, TAGS, accessor.getMessage("post.tags.empty"));
        ValidationUtils.rejectIfEmpty(errors, MATURE, accessor.getMessage("post.mature.empty"));
        
        final PostRequestUrlDto postRequestUrlDto = (PostRequestUrlDto) target;

        if (!CoverUtils.isValidUrl(postRequestUrlDto.getUrl())) {
            errors.rejectValue(URL, accessor.getMessage("post.cover.url.notValid"));
        }
    }
    
}
