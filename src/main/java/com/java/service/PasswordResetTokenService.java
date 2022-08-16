package com.java.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.java.entity.PasswordResetToken;

@Service
public interface PasswordResetTokenService extends JpaRepository<PasswordResetToken , String> {
	 
	  @SuppressWarnings("unchecked")
	  PasswordResetToken save(PasswordResetToken passwordResetToken);
	  
	  
	  @Query(value = "select * from password_reset_token where token = ?", nativeQuery = true)
	  public PasswordResetToken findByToken(String token);

}
