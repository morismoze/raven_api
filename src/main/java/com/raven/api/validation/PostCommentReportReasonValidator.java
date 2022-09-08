package com.raven.api.validation;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.raven.api.model.enums.PostCommentReportReasonValue;
import com.raven.api.request.PostCommentReportRequestDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostCommentReportReasonValidator implements Validator {

    private static final String REASON = "reason";

    private final MessageSourceAccessor accessor;

    @Override
    public boolean supports(Class<?> aClass) {
        return PostCommentReportRequestDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {        
        final PostCommentReportRequestDto postCommentReportRequestDto = (PostCommentReportRequestDto) target;
        
        final boolean isReasonValueValid = this.checkReasonValue(postCommentReportRequestDto.getReason().getReasonValue());
        if (!isReasonValueValid) {
            errors.rejectValue(REASON, this.accessor.getMessage("postComment.report.reason.notValid"));
        }

    }

    private boolean checkReasonValue(String userReasonValue) {
        for (PostCommentReportReasonValue reasonValue : PostCommentReportReasonValue.values()) {
            if (reasonValue.getValue().equals(userReasonValue)) {
                return true;
            }
        }

        return false;
    }
    
}
