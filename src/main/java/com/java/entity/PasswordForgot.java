package com.java.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class PasswordForgot {
	@NotEmpty(message = "{EMAIL_REQUIRED}")
	@Email(message = "{NOT_VALID_EMAIL}")
	private String email;

	public PasswordForgot(@NotEmpty(message = "{EMAIL_REQUIRED}") @Email(message = "{NOT_VALID_EMAIL}") String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PasswordForgot() {
		super();
	}

}
