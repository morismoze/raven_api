package com.raven.api.service.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.raven.api.exception.ServerErrorException;
import com.raven.api.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final int NO_OF_QUICK_SERVICE_THREADS = 20;

    private final JavaMailSender emailSender;

    private final ScheduledExecutorService quickService = Executors.newScheduledThreadPool(NO_OF_QUICK_SERVICE_THREADS);

    private final MessageSourceAccessor accessor;

    private String username = System.getenv("MAIL_USERNAME");

    @Override
    public void sendMessage(String to, String subject, String content, String contentType) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            message.setContent(content, contentType);
            message.setRecipients(Message.RecipientType.TO, to);
            message.setFrom(username);
            message.setSubject(subject);
        } catch (MessagingException messagingException) {
            // do nothing for now
        }
        quickService.submit(() -> {
            try {
                emailSender.send(message);
            } catch (Exception exception) {
                System.out.print(this.emailSender.toString());
                exception.printStackTrace();
                throw new ServerErrorException(this.accessor.getMessage("mail.error"));
            }
        });        
    }
    
}
