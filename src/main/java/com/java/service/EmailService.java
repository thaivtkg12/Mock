package com.java.service;

import java.util.List;

import com.java.entity.Email;

public interface EmailService {
	boolean checkExistsEmail(String emailName);
	Email addEmail(Email email);
	List<Email> getEmails();
	void deleteEmail(String emailName);
}
