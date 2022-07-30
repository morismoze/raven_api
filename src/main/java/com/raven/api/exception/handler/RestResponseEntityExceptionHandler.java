package com.raven.api.exception.handler;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.exception.ServerErrorException;
import com.raven.api.exception.UnauthorizedException;
import com.raven.api.response.Response;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler {

    private final MessageSourceAccessor accessor;
    
    @ExceptionHandler(value = {UnauthorizedException.class})
    protected ResponseEntity<?> handleUnauthorizedExeption(UnauthorizedException ex) {
        Response<?> response = Response.build(ex.getMessage(), true);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = {EntryNotFoundException.class})
    protected ResponseEntity<?> handleEntryNotFoundException(EntryNotFoundException ex) {
        Response<?> response = Response.build(ex.getMessage(), true);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = {ServerErrorException.class})
    protected ResponseEntity<?> handleServerErrorException(ServerErrorException ex) {
        Response<?> response = Response.build(ex.getMessage(), true);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<?> handleDefaultErrorException(Exception ex) {
        ex.printStackTrace();
        Response<?> response = Response.build(this.accessor.getMessage("server.error"), true);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
