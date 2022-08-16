package com.java.controller;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.context.Context;

import com.java.dto.ContactDTO;
import com.java.dto.EmailDTO;
import com.java.entity.Contact;
import com.java.service.ContactService;
import com.java.service.ShoppingCartService;
import com.java.service.WishListService;
import com.java.service.impl.EmailServiceImpl;
import com.java.utils.Constants;
import com.java.utils.Process;

@Controller
public class ContactController extends CommonController {

	@Autowired
	ContactService contactService;
	
	@Autowired
	ShoppingCartService shoppingCartService;
	
	@Autowired
	WishListService wishListService;

	@GetMapping(value = "/contact")
	public String contact(Model model) {

		model.addAttribute("totalCartItemWishs", wishListService.getCount());
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		return "site/contact";
	}
	
	@PostMapping(value = "/contact")
	public String submitContact(Model model, @ModelAttribute ContactDTO contactDTO) {
		
		InternetAddress[] internetAddress = new InternetAddress[1];
        EmailDTO emailDTO = new EmailDTO();
        EmailServiceImpl emailServiceImpl = new EmailServiceImpl();
        Context context = new Context();
        
        Contact contact = Process.convertContactDTOToContact(contactDTO);
        contact.setStatus((byte)0);
        
        try {
            internetAddress[0] = new InternetAddress(contact.getEmail());
            context.setVariable("customerName", contact.getCustomerName());
            emailDTO.setContext(context);
            emailDTO.setEmailTemplate(Constants.CONTACT_EMAIL_TEMPLATE);
            emailDTO.setSubject(contact.getSubject());
            emailDTO.setInternetAddresses(internetAddress);
            emailServiceImpl.setEmailDTO(emailDTO);
            emailServiceImpl.start();
            
        } catch (AddressException e) {
            System.err.println("Can not send gmai!");
        }
		
        contactService.addContact(contact);
        
		return contact(model);
	}
}
