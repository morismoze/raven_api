package com.raven.api.exception;

public class NotLoggedInException extends RuntimeException {

    public NotLoggedInException() {
    }

    public NotLoggedInException(final String message) {
        super(message);
    }

    public NotLoggedInException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotLoggedInException(final Throwable cause) {
        super(cause);
    }

    public NotLoggedInException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
