package com.raven.api.exception;

public class MissingTokenException extends RuntimeException {
    
    public MissingTokenException() {
    }

    public MissingTokenException(final String message) {
        super(message);
    }

    public MissingTokenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MissingTokenException(final Throwable cause) {
        super(cause);
    }

    public MissingTokenException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
