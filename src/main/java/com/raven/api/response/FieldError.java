package com.raven.api.response;

public class FieldError {

    private String field;
    
    private String error;

    public FieldError(final String field, final String error) {
        this.field = field;
        this.error = error;
    }

    public static FieldError from(final org.springframework.validation.FieldError error) {
        return new FieldError(error.getField(), error.getCode());
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

}
