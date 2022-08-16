package com.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
}
