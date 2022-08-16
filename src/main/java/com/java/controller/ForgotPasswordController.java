package com.java.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import com.java.dto.EmailDTO;
import com.java.entity.Customer;
import com.java.entity.PasswordForgot;
import com.java.entity.PasswordResetToken;
import com.java.repository.CustomersRepository;
import com.java.service.PasswordResetTokenService;
import com.java.service.impl.EmailServiceImpl;
import com.java.utils.Constants;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

	@Autowired
	CustomersRepository customersRepository;

	@Autowired
	PasswordResetTokenService passwordResetTokenService;

	@Autowired
	MessageSource messageSource;

	@GetMapping
	public String viewPage(Model model) {
		return "forgot-password";
	}
//===========================KHOA=========================================
	@PostMapping
	public String processPasswordForgot(@Valid @ModelAttribute("passwordForgot") PasswordForgot passwordForgot,
			BindingResult result, Model model, RedirectAttributes attributes, HttpServletRequest request) {

		Customer customer = customersRepository.findbyEmail(passwordForgot.getEmail());
		if (customer != null) {
			InternetAddress[] internetAddress = new InternetAddress[1];
	        EmailDTO emailDTO = new EmailDTO();
	        EmailServiceImpl emailServiceImpl = new EmailServiceImpl();
	        Context context = new Context();
			
			PasswordResetToken token = new PasswordResetToken();
			token.setCustomer(customer);
			token.setToken(UUID.randomUUID().toString());
			token.setExpirationDate(LocalDateTime.now().plusMinutes(30));
			passwordResetTokenService.save(token);
			
			try {
	            internetAddress[0] = new InternetAddress(customer.getEmail());
	            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	            
	            context.setVariable("token", token);
	            context.setVariable("user", customer);
	            context.setVariable("resetUrl", url + "/reset-password?token=" + token.getToken());
	            emailDTO.setContext(context);
	            emailDTO.setEmailTemplate(Constants.FORGOTPASSWORD_EMAIL_TEMPLATE);
	            emailDTO.setSubject("Forgot password!");
	            emailDTO.setInternetAddresses(internetAddress);
	            emailServiceImpl.setEmailDTO(emailDTO);
	            emailServiceImpl.start();
	            
	        } catch (AddressException e) {
	            System.err.println("Can not send gmai!");
	        }
		}

		return "redirect:/login";
	}

	@ModelAttribute("passwordForgot")
	public PasswordForgot passwordForgot() {
		return new PasswordForgot();
	}

}
