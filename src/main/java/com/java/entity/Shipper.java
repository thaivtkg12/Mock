package com.java.entity;



import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "shipper")
public class Shipper implements Serializable {

	@Id
	private String idshipper;
	private String user;
	private String name;
	private String password;

	public Shipper(String idshipper, String user, String name, String password) {
		super();
		this.idshipper = idshipper;
		this.user = user;
		this.name = name;
		this.password = password;
	}

	public String getIdshipper() {
		return idshipper;
	}

	public void setIdshipper(String idshipper) {
		this.idshipper = idshipper;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
