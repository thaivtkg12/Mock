package com.java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.entity.Category;
import com.java.entity.Order;
import com.java.entity.OrderDetail;
import com.java.entity.PasswordResetToken;
import com.java.entity.Product;
import com.java.repository.OrderDetailRepository;
import com.java.repository.OrderRepository;
import com.java.repository.ProductRepository;

@Controller
public class ShippingLogin {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@GetMapping(value = "/shipper")
	public String productShipper(Model model) {
		List<Object[]> products = orderRepository.listforship();
		model.addAttribute("products", products);
		return "site/ship";
	}
	
	@GetMapping(value = "/edit-status")
	public String viewPage(@RequestParam("id") Integer id) {
		System.out.println(id);
		List<OrderDetail> orderDetail = orderDetailRepository.findByOrderId(id);
		for (OrderDetail orderDetail2 : orderDetail) {
			orderDetail2.setStatus("Da Thanh Toan");
			orderDetailRepository.save(orderDetail2);	
		}
		return "redirect:/carts";
	}
	
	

	


}
