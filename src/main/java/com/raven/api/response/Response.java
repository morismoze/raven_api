package com.raven.api.response;

import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Response<T> {

    private T data;
    private boolean hasErrors;
    private String message;
    private List<FieldError> fieldErrors;

    public static <T> Response<T> build(final T data) {
        final Response<T> response = new Response<>();
        response.setData(data);
        response.setFieldErrors(Collections.emptyList());

        return response;
    }

    public static <T> Response<T> build(final Errors errors) {
        final Response<T> response = new Response<>();

        if (errors.hasErrors()) {
            response.setHasErrors(true);

            response.setFieldErrors(errors.getFieldErrors()
                    .stream()
                    .map(FieldError::from)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    public static <T> Response<T> build(final String message, final boolean isError) {
        final Response<T> response = new Response<>();
        response.setHasErrors(isError);
        response.setMessage(message);
        response.setFieldErrors(Collections.emptyList());

        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public List<FieldError> getFieldErrors() {
        return Collections.unmodifiableList(fieldErrors);
    }

    public void setFieldErrors(final List<FieldError> fieldErrors) {
        this.fieldErrors = List.copyOf(fieldErrors);
    }

}
