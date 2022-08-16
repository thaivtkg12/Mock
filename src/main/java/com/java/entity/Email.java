package com.java.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "email_name")
	private String emailName;

	@Column(name = "register_date")
	private Date registerDate = new Date();

	public Email(String emailName) {
		super();
		this.emailName = emailName;
	}
}