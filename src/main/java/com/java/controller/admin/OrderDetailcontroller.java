package com.java.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.java.entity.Customer;
import com.java.entity.Order;
import com.java.service.CustomerService;
import com.java.service.OrderService;
import com.java.utils.Process;

@Controller
public class OrderDetailcontroller {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/admin/order-detail/{id}")
	public String getOrderDetail(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		Order order;
		int orderID;
		
		Customer customer = customerService.findCustomerByID(request.getRemoteUser());
		
		if (Process.checkInteger(id)) {
			orderID = Integer.parseInt(id);
			if (orderService.checkExistsOrderID(orderID)) {
				order = orderService.getOrderByID(orderID);
				
				model.addAttribute("order", order);
				model.addAttribute("customer", customer);
				return "admin/orderdetail";
			} else {
				return "redirect:/admin/orders";
			}
		} else {
			return "redirect:/admin/orders";
		}
		
		
	}
}
