package com.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.entity.Email;
import com.java.service.EmailService;

@RestController
public class EmailController {
	
	@Autowired
	EmailService emailService;
	
	@PostMapping("/api/email")
	public void addEmail(@RequestBody Email email) {
		if (emailService.checkExistsEmail(email.getEmailName()) != true) {
			emailService.addEmail(email);
		}
	}
}
