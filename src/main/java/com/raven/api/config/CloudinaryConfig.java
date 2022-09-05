package com.raven.api.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Value(value = "${cloudinary.api-key}")
	private String apiKey;

    @Value(value = "${cloudinary.secret}")
	private String secret;

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
    
}
