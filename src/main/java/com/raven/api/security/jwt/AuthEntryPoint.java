package com.raven.api.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raven.api.response.Response;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        // @todo: make throwing custom AuthenticationExceptions work

        int UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Response<Integer> responseBuild = Response.build(String.valueOf(UNAUTHORIZED), true);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBuild);
    }

}
