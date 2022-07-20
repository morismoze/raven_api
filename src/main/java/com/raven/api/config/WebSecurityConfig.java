package com.raven.api.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.raven.api.security.AuthFailureHandler;
import com.raven.api.security.jwt.AuthEntryPoint;
import com.raven.api.security.jwt.AuthenticationTokenFilter;
import com.raven.api.security.jwt.AuthorizationTokenFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final MessageSourceAccessor accessor;

    private final AuthEntryPoint authEntryPoint;

    private final AuthorizationTokenFilter authorizationTokenFilter;

    private final AuthFailureHandler authFailureHandler;

    @Value(value = "${jwt.secret}")
	private String secret;

	@Value(value = "${jwt.claim}")
	private String claim;

	@Value(value = "${jwt.access-token-expiration-time-millis}")
	private Long accessTokenExpirationTimeMillis;

	@Value(value = "${jwt.refresh-token-expiration-time-millis}")
	private Long refreshTokenExpirationTimeMillis;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(this.authEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers("/user/create", "/login", "user/token/refresh").permitAll()
            .anyRequest().authenticated();
        http.addFilter(new AuthenticationTokenFilter(
            authenticationManagerBean(), 
            this.secret, 
            this.claim, 
            this.accessTokenExpirationTimeMillis, 
            this.refreshTokenExpirationTimeMillis,
            this.accessor));
        http.addFilterBefore(this.authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
