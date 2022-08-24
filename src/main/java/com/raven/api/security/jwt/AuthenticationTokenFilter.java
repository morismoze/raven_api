package com.raven.api.security.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raven.api.exception.EntryNotFoundException;
import com.raven.api.exception.ServerErrorException;
import com.raven.api.exception.UnauthorizedException;
import com.raven.api.mapper.UserMapper;
import com.raven.api.model.User;
import com.raven.api.request.UserRequestDto;
import com.raven.api.response.Response;
import com.raven.api.response.UserResponseDto;
import com.raven.api.service.UserService;

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MessageSourceAccessor accessor;
    
	@Value(value = "${jwt.secret}")
	private String secret;

	@Value(value = "${jwt.claim}")
	private String claim;

	@Value(value = "${jwt.access-token-expiration-time-millis}")
	private Long accessTokenExpirationTimeMillis;

	@Value(value = "${jwt.refresh-token-expiration-time-millis}")
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
		} catch (IOException ioException) {
			throw new ServerErrorException(this.accessor.getMessage("server.error"));
		} catch (DisabledException disabledException) {
			throw new UnauthorizedException(this.accessor.getMessage("user.disabledAccount"));
		} catch (LockedException lockedException) {
			throw new UnauthorizedException(this.accessor.getMessage("user.lockedAccount"));
		} catch (BadCredentialsException | EntryNotFoundException badCredentialsException) {
			throw new UnauthorizedException(this.accessor.getMessage("user.incorrectCredentials"));
		} catch (AuthenticationException entryNotFoundException) {
			throw new UnauthorizedException(this.accessor.getMessage("user.incorrectCredentials"));
		}
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		org.springframework.security.core.userdetails.User userPrincipal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
		String accessToken = JWT.create()
			.withSubject(userPrincipal.getUsername())
			.withExpiresAt(new Date(new Date().getTime() + this.accessTokenExpirationTimeMillis))
			.withIssuer(request.getRequestURL().toString())
			.withClaim(this.claim, userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.sign(algorithm);
		String refreshToken = JWT.create()
			.withSubject(userPrincipal.getUsername())
			.withExpiresAt(new Date(new Date().getTime() + this.refreshTokenExpirationTimeMillis))
			.withIssuer(request.getRequestURL().toString())
			.sign(algorithm);	
		User user = this.userService.findByUsername(userPrincipal.getUsername());
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setHeader("access_token", accessToken);
		response.setHeader("refresh_token", refreshToken);
		UserResponseDto userResponseDto = this.userMapper.userUserResponseDtoMapper(user);
		Response<UserResponseDto> responseBuild = Response.build(userResponseDto);

		new ObjectMapper().writeValue(response.getOutputStream(), responseBuild);
	}
    
}
