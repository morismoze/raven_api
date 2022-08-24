package com.raven.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application-${spring.profiles.active}.yml")
public class ApiApplication {

	public static void main(String[] args) {
		System.out.println(System.getenv("MAIL_USERNAME"));
		SpringApplication.run(ApiApplication.class, args);
	}

}
