package com.java.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.entity.Customer;
import com.java.repository.CustomersRepository;
import com.java.service.CustomerService;

@Service
public class CustomerServiceimpl  implements CustomerService{
	@Autowired
	CustomersRepository customersRepository;
	
	@Override
	public void updateCustomer(Customer customer) {
		customersRepository.save(customer);
	}

	@Override
	public Customer findCustomerByID(String id) {
		return customersRepository.findbyEmail(id);
	}
	
	

}
