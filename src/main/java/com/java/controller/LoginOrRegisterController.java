package com.java.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;

import com.java.dto.EmailDTO;
import com.java.entity.Customer;
import com.java.entity.OAuth;
import com.java.repository.CustomersRepository;
import com.java.service.GoogleUtils;
import com.java.service.impl.EmailServiceImpl;
import com.java.utils.Constants;

@Controller
public class LoginOrRegisterController extends CommonController {

	@Autowired
	CustomersRepository customersRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private GoogleUtils googleUtils;
//==============================KHOA================================================
	@RequestMapping("/login-google")
	public String loginGoogle(HttpServletRequest request, Customer customer, Model model)
			throws ClientProtocolException, IOException {
		InternetAddress[] internetAddress = new InternetAddress[1];
        EmailDTO emailDTO = new EmailDTO();
        EmailServiceImpl emailServiceImpl = new EmailServiceImpl();
        Context context = new Context();
        
		String code = request.getParameter("code");
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		if (code == null || code.isEmpty()) {
			return "redirect:/login?google=error";
		}

		String accessToken = googleUtils.getToken(code);
		OAuth Oauth = googleUtils.getUserInfo(accessToken);
		UserDetails userDetail = googleUtils.buildUser(Oauth);
		String user = userDetail.getUsername();
		String[] userName=user.split("@");
		customer.setCustomerId(userName[0]);
		customer.setEmail(user);
		customer.setFullname("user");
		customer.setEnabled(true);
		customer.setRoleId("0");
		customer.setPassword(bCryptPasswordEncoder.encode(generatedString));
		Customer c = customersRepository.save(customer);

		try {
            internetAddress[0] = new InternetAddress(c.getEmail());
            context.setVariable("customerID", c.getCustomerId());
            context.setVariable("password", generatedString);
            emailDTO.setContext(context);
            emailDTO.setEmailTemplate(Constants.REGISTER_EMAIL_TEMPLATE);
            emailDTO.setSubject("Register!");
            emailDTO.setInternetAddresses(internetAddress);
            emailServiceImpl.setEmailDTO(emailDTO);
            emailServiceImpl.start();
            
        } catch (AddressException e) {
            System.err.println("Can not send gmai!");
        }
		
		return "site/loginOrRegister";
	}
//=================================KHOA===========================================
	@GetMapping(value = "/login")
	public String loginOrRegister(Model model, @RequestParam("error") Optional<String> error, HttpServletRequest request) {
		
		if (request.getRemoteUser() != null) {
			return "site/index";
		} else {
			String errorString = error.orElse("false");
			if (errorString.equals("true")) {
				model.addAttribute("error", "T??i kho???n ho???c m???t kh???u kh??ng ch??nh x??c, vui l??ng th??? l???i!");
			}

			model.addAttribute("customer", new Customer());
			return "site/loginOrRegister";
		}
	}
//=================================KHOA===========================================

	@SuppressWarnings("unused")
	@RequestMapping(value = "/registered")
	public String addCourse(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, ModelMap model,
			Principal principal) {
		Random rand = new Random();

		// check error
		if (result.hasErrors()) {
			return "site/loginOrRegister";
		}

		// check email by database
		if (!checkEmail(customer.getEmail())) {
			model.addAttribute("error", "????ng k?? th???t b???i, Email n??y ???? ???????c s??? d???ng!");
			return "site/loginOrRegister";
		}

		// check id login by database
		if (!checkIdlogin(customer.getCustomerId())) {
			model.addAttribute("error", "????ng k?? th???t b???i, ID Login n??y ???? ???????c s??? d???ng!");
			return "site/loginOrRegister";
		}

	
		customer.setEnabled(true);
		customer.setRoleId("0");
		customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));

		Customer c = customersRepository.save(customer);
//		sendMailService.sendMail(c.getEmail(), " Store",
//				"<h3>Hi:" + c.getFullname() + " !</h3> Ch??c m???ng b???n ???? ????ng k?? t??i kho???n th??nh c??ng \r\n ");
//		Role role = new Role();
//		role.setRoleName("ROLE_USER");
//		role.setCustomer(customer);
		if (null != c) {
			model.addAttribute("message", "????ng k?? th??nh c??ng, xin m???i ????ng nh???p!");
			model.addAttribute("customer", customer);
		} else {
			model.addAttribute("error", "failure");
			model.addAttribute("customer", customer);
		}

		return "site/loginOrRegister";
	}

	// check email
	public boolean checkEmail(String email) {
		List<Customer> list = customersRepository.findAll();
		for (Customer c : list) {
			if (c.getEmail().equalsIgnoreCase(email)) {
				return false;
			}
		}
		return true;
	}

	// check ID Login
	public boolean checkIdlogin(String customerId) {
		List<Customer> listC = customersRepository.findAll();
		for (Customer c : listC) {
			if (c.getCustomerId().equalsIgnoreCase(customerId)) {
				return false;
			}
		}
		return true;
	}

}
