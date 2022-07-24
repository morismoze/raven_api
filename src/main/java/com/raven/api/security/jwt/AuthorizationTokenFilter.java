package com.raven.api.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Arrays.stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.raven.api.exception.ServerErrorException;

@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {

    private MessageSourceAccessor accessor;

	@Value(value = "${jwt.secret}")
	private String secret;

	@Value(value = "${jwt.claim}")
	private String claim;

    private final String TOKEN_TYPE = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().equals("/login") 
            || request.getServletPath().equals("/user/token/refresh")
            || request.getServletPath().equals("/user/create")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_TYPE)) {
                try {
                    String token = authorizationHeader.substring(TOKEN_TYPE.length());
                    Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim(this.claim).asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (IllegalArgumentException illegalArgumentException) {
                    illegalArgumentException.printStackTrace();
                } catch (TokenExpiredException tokenExpiredException) {
                    tokenExpiredException.printStackTrace();
                    response.setHeader("error", "expired_access_token");
                    response.sendError(HttpStatus.UNAUTHORIZED.value());
                } catch (Exception e) {
                    throw new ServerErrorException(this.accessor.getMessage("server.error"));
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
    
}
