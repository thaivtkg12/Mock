package com.java.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.entity.Email;
import com.java.repository.EmailRepository;
import com.java.service.EmailService;

@Service
public class EmailServiceImp implements EmailService{

	@Autowired
	EmailRepository emailRepository;
	
	@Override
	public boolean checkExistsEmail(String emailName) {
		return emailRepository.existsEmailByEmailName(emailName);
	}

	@Override
	public Email addEmail(Email email) {
		return emailRepository.save(email);
	}

	@Override
	public void deleteEmail(String emailName) {
		emailRepository.deleteById(emailName);
	}

	@Override
	public List<Email> getEmails() {
		return emailRepository.findAll();
	}

}
