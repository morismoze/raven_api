package com.raven.api.validation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.raven.api.request.PostRequestUrlDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostCommentRequestDtoValidator implements Validator {

    private static final String COMMENT = "comment";

    private final MessageSourceAccessor accessor;

    @Override
    public boolean supports(Class<?> aClass) {
        return PostRequestUrlDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, COMMENT, accessor.getMessage("post.postComment.empty"));
    }
    
}
