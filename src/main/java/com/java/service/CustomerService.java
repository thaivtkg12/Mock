package com.java.service;

import com.java.entity.Customer;


public interface CustomerService {
	
	void updateCustomer(Customer customer);
	Customer findCustomerByID(String id);
}
