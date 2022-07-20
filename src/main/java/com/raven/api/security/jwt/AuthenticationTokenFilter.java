package com.raven.api.security.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raven.api.exception.UnauthorizedException;
import com.raven.api.model.User;
import com.raven.api.request.UserRequestDto;
import com.raven.api.response.Response;
import com.raven.api.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private UserService userService;
	
	private MessageSourceAccessor accessor;
    
	private String secret;

	private String claim;

	private Long accessTokenExpirationTimeMillis;

	private Long refreshTokenExpirationTimeMillis;

    @Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserRequestDto userRequestDto = mapper.readValue(request.getInputStream(), UserRequestDto.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userRequestDto.getUsername(), userRequestDto.getPassword()
			);

			return this.authenticationManager.authenticate(authToken);
		} catch (IOException e) {
			throw new UnauthorizedException(this.accessor.getMessage("user.notAuthorized"));
		}
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		org.springframework.security.core.userdetails.User userPrincipal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
		String accessToken = JWT.create()
			.withSubject(userPrincipal.getUsername())
			.withExpiresAt(new Date(this.accessTokenExpirationTimeMillis))
			.withIssuer(request.getRequestURL().toString())
			.withClaim(this.claim, userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.sign(algorithm);

		String refreshToken = JWT.create()
			.withSubject(userPrincipal.getUsername())
			.withExpiresAt(new Date(this.refreshTokenExpirationTimeMillis))
			.withIssuer(request.getRequestURL().toString())
			.sign(algorithm);

		User user = this.userService.findUserByUsername(userPrincipal.getUsername());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setHeader("access_token", accessToken);
		response.setHeader("refresh_token", refreshToken);
		Response<User> responseBuild = Response.build(user);

		new ObjectMapper().writeValue(response.getOutputStream(), responseBuild);
	}
    
}
