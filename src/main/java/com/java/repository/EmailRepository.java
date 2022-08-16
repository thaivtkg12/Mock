package com.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.entity.Email;

public interface EmailRepository extends JpaRepository<Email, String>{
	boolean existsEmailByEmailName(String emailName);
}
