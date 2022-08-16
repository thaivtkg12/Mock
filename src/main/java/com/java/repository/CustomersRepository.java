package com.java.repository;

import java.text.AttributedString;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.entity.Customer;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Integer>{
	
	@Query(value = "select * from customers where customer_id = ?", nativeQuery = true)
    public Customer findCustomersLogin (String customerId);
	
	@Query(value = "select * from customers where email = ?", nativeQuery = true)
	Optional<Customer> FindByEmail(String email);
	
	@Query(value = "SELECT * FROM customers", nativeQuery = true)
	 public List<Customer> findallUser ();
	
	
	@Query(value = "Select  * from customers where email = ?", nativeQuery = true)
    public Customer findbyEmail (String email);
	
//	
	
}
