package com.raven.api.validation;

import java.util.regex.Pattern;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.request.UserRequestDto;
import com.raven.api.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRequestDtoValidator implements Validator {

    private static final String PASSWORD = "password";

    private static final String EMAIL = "email";

    private static final String FIRST_NAME = "firstName";

    private static final String LAST_NAME = "lastName";

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final UserService userService;

    private final MessageSourceAccessor accessor;

    @Override
    public boolean supports(final Class<?> aClass) {
        return UserRequestDto.class.equals(aClass);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, PASSWORD, accessor.getMessage("user.password.empty"));
        ValidationUtils.rejectIfEmpty(errors, FIRST_NAME, accessor.getMessage("user.firstName.empty"));
        ValidationUtils.rejectIfEmpty(errors, LAST_NAME, accessor.getMessage("user.lastName.empty"));
        
        final UserRequestDto user = (UserRequestDto) target;
    
        checkEmail(user, errors);
    }
    
    private void checkEmail(final UserRequestDto user, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, EMAIL, accessor.getMessage("user.email.empty"));
        if (user.getEmail() != null) {
            if (!VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail()).find())
                errors.rejectValue(EMAIL, accessor.getMessage("user.email.notValid"));
            try {
                userService.findUserByEmail(user.getEmail());
                
                // if exception was thrown, won't add to errors
                
                errors.rejectValue(EMAIL, accessor.getMessage("user.email.exists"));
            } catch (EntryNotFoundException e) {
                // Email doesn't exist
            }
        }
    }
}
