package com.raven.api.service;

public interface EmailService {
    
    void sendMessage(String to, String subject, String content, String contentType);

}
