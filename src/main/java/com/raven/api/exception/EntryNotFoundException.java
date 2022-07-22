package com.raven.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntryNotFoundException extends RuntimeException {

    public EntryNotFoundException() {
    }

    public EntryNotFoundException(final String message) {
        super(message);
    }

    public EntryNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntryNotFoundException(final Throwable cause) {
        super(cause);
    }

    public EntryNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
