package com.java.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PasswordResetToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(nullable = false, unique = true)
	private String token;
	@OneToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
	private Customer customer;
	@Column(nullable = false)
	private LocalDateTime expirationDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

}