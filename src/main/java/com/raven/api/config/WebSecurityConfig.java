package com.raven.api.config;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.raven.api.mapper.UserMapper;
import com.raven.api.security.jwt.AuthEntryPoint;
import com.raven.api.security.jwt.AuthenticationTokenFilter;
import com.raven.api.security.jwt.AuthorizationTokenFilter;
import com.raven.api.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final MessageSourceAccessor accessor;

    private final AuthEntryPoint authEntryPoint;

    private final AuthorizationTokenFilter authorizationTokenFilter;

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
            .formLogin().disable()
            .logout().permitAll()
            .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers("/user/create", "/login", "/logout", "/user/token/refresh").permitAll().and()
            .authorizeRequests().antMatchers("/tag/**").permitAll().and()
            .authorizeRequests().antMatchers("/post/**").permitAll().and()
            .authorizeRequests().antMatchers(
                "/post/file/create", 
                "/post/file/create",
                "post/{webId}/comments/create").hasAnyRole("ROLE_USER", "ROLE_ADMIN")
            .anyRequest().authenticated();
        http.addFilter(new AuthenticationTokenFilter(
            authenticationManagerBean(),
            this.userService,
            this.userMapper,
            this.accessor,
            this.secret, 
            this.claim, 
            this.accessTokenExpirationTimeMillis, 
            this.refreshTokenExpirationTimeMillis));
        http.addFilterBefore(this.authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With","Origin","Content-Type","Accept","Authorization"));
        
        // This allow us to expose the headers
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers", "access_token", "refresh_token", "error"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
