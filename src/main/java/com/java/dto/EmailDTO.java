package com.java.dto;

import javax.mail.internet.InternetAddress;
import org.thymeleaf.context.Context;

public class EmailDTO {
    
    private InternetAddress[] internetAddresses;
    private String subject;
    private String emailTemplate;
    private Context context;
    
    public EmailDTO() {
        super();
    }
    
    public InternetAddress[] getInternetAddresses() {
        return internetAddresses;
    }
    
    public void setInternetAddresses(InternetAddress[] internetAddresses) {
        this.internetAddresses = internetAddresses;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getEmailTemplate() {
        return emailTemplate;
    }
    
    public void setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
    }
    
    public Context getContext() {
        return context;
    }
    
    public void setContext(Context context) {
        this.context = context;
    }
    
}