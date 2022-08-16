package com.java.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.java.entity.Category;
import com.java.entity.Customer;
import com.java.entity.Product;
import com.java.repository.CategoryRepository;
import com.java.repository.CustomersRepository;
import com.java.repository.OrderDetailRepository;
import com.java.repository.ProductRepository;

@Controller
public class IndexAdminController {
	
	@Autowired
	CustomersRepository customersRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@GetMapping(value = "admin/home")
	public String indexAdmin(Model model, Principal principal) {
		model.addAttribute("customer", new Customer());
		Customer customer = customersRepository.FindByEmail(principal.getName()).get();
		model.addAttribute("customer", customer);	
		
		
		List<Customer> customer2 = customersRepository.findallUser();
		model.addAttribute("user", customer2.size());
		
		
		List<Product> products = productRepository.findAll();
		model.addAttribute("product", products.size());
		
		
		List<Category> categories = categoryRepository.listCategories();
		model.addAttribute("categories", categories.size());
		
		
		List<Integer> oderDetails = orderDetailRepository.doanhthuDetails();
		model.addAttribute("doanhthu", oderDetails.get(0));
		
		
		List<Integer> giaohangThanhcong = orderDetailRepository.succeesOder();
		model.addAttribute("thanhcong", giaohangThanhcong.get(0));
		
		
		List<Integer> dangiaoHang = orderDetailRepository.comingOder();
		model.addAttribute("dangxuly", dangiaoHang.get(0));
		
		
		return "admin/index";
	}

}
