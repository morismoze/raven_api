package com.raven.api.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raven.api.response.Response;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        int UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Response<Integer> responseBuild = Response.build(UNAUTHORIZED);
		new ObjectMapper().writeValue(response.getOutputStream(), responseBuild);
        System.out.println(exception);
    }

}
