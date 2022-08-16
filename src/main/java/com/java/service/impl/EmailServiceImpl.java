package com.java.service.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.java.dto.EmailDTO;
import com.java.service.ThymeleafService;
import com.java.utils.Constants;

public class EmailServiceImpl extends Thread{
	
	private EmailDTO emailDTO;
	
	public EmailServiceImpl() {
		
	}
	
	public EmailServiceImpl(EmailDTO emailDTO) {
		this.emailDTO = emailDTO;
	}
	
	public EmailDTO getEmailDTO() {
		return emailDTO;
	}
	
	public void setEmailDTO(EmailDTO emailDTO) {
		this.emailDTO = emailDTO;
	}
	
	@Override
    public void run() {
        sendMail();
    }
	
	public void sendMail() {
		
		ThymeleafService thymeleafService = new ThymeleafService();
        Properties props = new Properties();
        props.put("mail.smtp.host", Constants.HOST_GMAIL);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", Constants.PORT_GMAIL);



       Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Constants.USER_NAME_GMAIL, Constants.PASSWORD_GMAIL);
                    }
                });
        Message message = new MimeMessage(session);
        
        try {
            message.setRecipients(Message.RecipientType.TO, emailDTO.getInternetAddresses());
            message.setFrom(new InternetAddress(Constants.USER_NAME_GMAIL));
            message.setSubject(emailDTO.getSubject());
            message.setContent(thymeleafService.getContent(emailDTO.getEmailTemplate(),
                                                           emailDTO.getContext()),
                                                           Constants.CONTENT_TYPE_TEXT_HTML);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
		
	}
}
