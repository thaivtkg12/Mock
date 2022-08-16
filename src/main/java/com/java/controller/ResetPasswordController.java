package com.java.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.entity.Customer;
import com.java.entity.PasswordReset;
import com.java.entity.PasswordResetToken;
import com.java.service.CustomerService;
import com.java.service.PasswordResetTokenService;

@Controller
@RequestMapping("/reset-password")
public class ResetPasswordController {

	@Autowired
	PasswordResetTokenService tokenService;
	@Autowired
	CustomerService customerService;
	

	@GetMapping
	public String viewPage(@RequestParam(name = "token", required = false) String token, Model model) {
		PasswordResetToken passwordResetToken = tokenService.findByToken(token);
		model.addAttribute("token", passwordResetToken.getToken());
		return "reset-password";
	}

	@PostMapping
	public String resetPassword(@ModelAttribute("passwordReset") PasswordReset passwordReset) {
		PasswordResetToken token = tokenService.findByToken(passwordReset.getToken());
		Customer customer = token.getCustomer();
		customer.setPassword(passwordReset.getPassword());
		customerService.updateCustomer(customer);
		return "redirect:/login";
	}

	@ModelAttribute("passwordReset")
	public PasswordReset passwordReset() {
		return new PasswordReset();
	}

}
