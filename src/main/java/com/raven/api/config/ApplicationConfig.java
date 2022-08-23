package com.raven.api.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.AntPathMatcher;

import com.cloudinary.Cloudinary;

@Configuration
public class ApplicationConfig {

    @Value(value = "${cloudinary.api-key}")
	private String apiKey;

    @Value(value = "${cloudinary.secret}")
	private String secret;
    
    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        final ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages", "classpath:mail");
        messageSource.setDefaultEncoding("UTF-8");
        return new MessageSourceAccessor(messageSource);
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = null;
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "cdn-cloudinary");
        config.put("api_key", this.apiKey);
        config.put("api_secret", this.secret);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
    
}
