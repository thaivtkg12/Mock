package com.java.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.entity.Customer;
import com.java.entity.Order;
import com.java.repository.CustomersRepository;
import com.java.repository.OrderRepository;
import com.java.service.CustomerService;
import com.java.service.OrderService;
import com.java.service.ShoppingCartService;
import com.java.service.WishListService;
import com.java.utils.Process;

@Controller
public class AccountController extends CommonController {

	@Autowired
	CustomerService customerService;

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	CustomersRepository customersRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderService orderService;
	
	@Autowired
	WishListService wishListService;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping(value = "/account")
	public String account(Model model, Principal principal) {

		Customer customer = customersRepository.FindByEmail(principal.getName()).get();
		List<Order> listO2 = orderRepository.findByCustomerId(customer.getCustomerId());

		model.addAttribute("customer", new Customer());
		model.addAttribute("customer", customer);
		model.addAttribute("orders2", listO2);
		model.addAttribute("totalCartItemWishs", wishListService.getCount());
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		return "site/account";
	}

//=========================KHOA===============================
	@PostMapping(value = "/account")
	public String updateProfile(Model model, Principal principal, @RequestParam("fullName") String fullName,
			@RequestParam("email") String email, @RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) {

		Customer customer = customersRepository.FindByEmail(principal.getName()).get();
		if (bCryptPasswordEncoder.matches(oldPassword, customer.getPassword())) {
			customer.setEmail(email);
			customer.setFullname(fullName);
			customer.setPassword(bCryptPasswordEncoder.encode(newPassword));

			customerService.updateCustomer(customer);
		}

		return account(model, principal);
	}

	@GetMapping("account/cancel-order/{id}")
	public String cancelOrder(@PathVariable("id")String id, HttpServletRequest request) {
		if(Process.checkInteger(id)) {
			int orderId=Integer.parseInt(id);
			String userId = request.getRemoteUser();
			Order order=orderService.findByOrderIdAndEmail(orderId, userId);
			if(order!=null) {
				if(order.getStatus()==0) {
					order.setStatus(2);
					orderService.updateOrder(order);
				}
			}
		}
		return "redirect:/account";
	}

}
