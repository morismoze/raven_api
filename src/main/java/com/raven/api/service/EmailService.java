package com.raven.api.service;

import java.util.Map;

public interface EmailService {

    String generateHtmlString(String templateName, Map<String, Object> variables);
    
    void sendMessage(String to, String subject, String content, String contentType);

}
