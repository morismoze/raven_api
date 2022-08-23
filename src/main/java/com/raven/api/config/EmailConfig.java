package com.raven.api.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource(value = "classpath:mail.properties", encoding = "UTF-8")
public class EmailConfig {

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${host}")
    private String host;

    @Value("${port}")
    private Integer port;

    @Value("${mail.transport.protocol}")
    private String mailTransportProtocol;

    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private String mailSmtpStarttlsEnable;

    @Value("${mail.debug}")
    private String mailDebug;
    
    @Bean
	public JavaMailSender mailSender() {
		final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(this.host);
		mailSender.setPort(this.port);
		mailSender.setUsername(this.username);
		mailSender.setPassword(this.password);

		final Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.transport.protocol", mailTransportProtocol);
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        props.put("mail.debug", mailDebug);

		return mailSender;
	}
    
}
