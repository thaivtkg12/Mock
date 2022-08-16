package com.java.controller.admin;

import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;

import com.java.dto.EmailDTO;
import com.java.entity.Customer;
import com.java.entity.Email;
import com.java.repository.CustomersRepository;
import com.java.service.EmailService;
import com.java.service.impl.EmailServiceImpl;
import com.java.utils.Constants;

@Controller
public class EmailsController {

	@Autowired
	EmailService emailService;

	@Autowired
	CustomersRepository customersRepository;

	@GetMapping("/admin/emails")
	public String getEmailManagerPage(Model model, HttpServletRequest request) {
		String idString = request.getRemoteUser();

		Customer customer = customersRepository.findbyEmail(idString);

		List<Email> emails = emailService.getEmails();

		model.addAttribute("emails", emails);
		model.addAttribute("customer", customer);

		return "admin/email";
	}

	@PostMapping("/admin/emails")
	public String sendMSGEmail(@RequestParam("subject") String subject, @RequestParam("emailContent") String content) throws AddressException {
		List<Email> emails = emailService.getEmails();
		InternetAddress[] internetAddress = new InternetAddress[emails.size()];
		EmailDTO emailDTO = new EmailDTO();
		EmailServiceImpl emailServiceImpl = new EmailServiceImpl();
		Context context = new Context();
		int i = 0;

		for (Email email : emails) {
			internetAddress[i++] = new InternetAddress(email.getEmailName());
		}
		
		context.setVariable("content", content);
		emailDTO.setContext(context);
		emailDTO.setEmailTemplate(Constants.NOTIFICATION_EMAIL_TEMPLATE);
		emailDTO.setSubject(subject);
		emailDTO.setInternetAddresses(internetAddress);
		emailServiceImpl.setEmailDTO(emailDTO);
		emailServiceImpl.start();

		return "redirect:/admin/emails";
	}
	
	@GetMapping("/admin/delete-email/{id}")
	public String deleteEmail(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		
		if (emailService.checkExistsEmail(id)) {
			emailService.deleteEmail(id);
		}
		
		return getEmailManagerPage(model, request);
	}

}
