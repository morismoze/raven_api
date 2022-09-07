package com.raven.api.service.impl;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.raven.api.exception.ServerErrorException;
import com.raven.api.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    
    private final ScheduledExecutorService quickService = Executors.newScheduledThreadPool(NO_OF_QUICK_SERVICE_THREADS);

    private final TemplateEngine templateEngine;
    
    private final MessageSourceAccessor accessor;
    
    private String username = System.getenv("MAIL_USERNAME");

    private static final int NO_OF_QUICK_SERVICE_THREADS = 20;

    @Override
    public String generateHtmlString(String templateName, Map<String, Object> variables) {
        return this.templateEngine.process(templateName, new Context(Locale.getDefault(), variables));
    }

    @Override
    public void sendMessage(String to, String subject, String content, String contentType) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            message.setContent(content, contentType);
            message.setRecipients(Message.RecipientType.TO, to);
            message.setFrom(this.username);
            message.setSubject(subject);
        } catch (MessagingException messagingException) {
            // do nothing for now
        }
        quickService.submit(() -> {
            try {
                emailSender.send(message);
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new ServerErrorException(this.accessor.getMessage("mail.error"));
            }
        });        
    }
    
}
