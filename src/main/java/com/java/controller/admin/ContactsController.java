package com.java.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.java.entity.Contact;
import com.java.entity.Customer;
import com.java.service.ContactService;
import com.java.service.CustomerService;
import com.java.utils.Process;

@Controller
public class ContactsController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ContactService contactService;
	
	@GetMapping("/admin/contacts")
	public String getContactsMNG(Model model, HttpServletRequest request) {
		
		String idString = request.getRemoteUser();
		Customer customer = customerService.findCustomerByID(idString);
		List<Contact> contacts = contactService.getContacts();
		
		model.addAttribute("customer", customer);
		model.addAttribute("contacts", contacts);
		
		return "admin/contact";
	}
	
	@GetMapping("/admin/contact/update-status/{id}")
	public String updateStatus(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		Contact contact;
		
		if (Process.checkPageParameter(id)) {
			contact = contactService.getContact(Integer.parseInt(id));
			if (contact.getStatus() == 0) {
				contact.setStatus((byte)1);
				contactService.updateContact(contact);
			}
		}
		
		return "redirect:/admin/contacts";
	}
}
