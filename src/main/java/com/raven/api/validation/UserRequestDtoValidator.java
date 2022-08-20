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

    private static final String USERNAME = "username";

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
        checkUsername(user, errors);
    }
    
    
    private void checkEmail(final UserRequestDto user, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, EMAIL, accessor.getMessage("user.email.empty"));

        final String email = user.getEmail();
        if (email != null) {
            if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find())
            errors.rejectValue(EMAIL, accessor.getMessage("user.email.notValid"));
            try {
                this.userService.findByEmail(email);
                errors.rejectValue(EMAIL, accessor.getMessage("user.email.exists"));
            } catch (EntryNotFoundException e) {
                // if exception was thrown - email doesn't exist - won't add to errors
            }
        }
    }
    
    private void checkUsername(final UserRequestDto user, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, EMAIL, accessor.getMessage("user.username.empty"));
        
        final String username = user.getUsername();
        if (user.getUsername() != null) {
            try {
                this.userService.findByUsername(username);
                errors.rejectValue(USERNAME, accessor.getMessage("user.username.exists"));
            } catch (EntryNotFoundException e) {
                // if exception was thrown - username doesn't exist - won't add to errors
            }
        }
    }
    
}
