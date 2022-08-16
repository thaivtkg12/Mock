package com.java.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;



@SuppressWarnings("serial")
@Entity
@Table(name = "orders")
public class Order implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer orderId;
	private String phone;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private Date orderDate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private Date requireDate;
	private String receiver;
	private String address;
	private String description;
	private Double amount;
	@Column(name = "total_price")
	private Double totalPrice;
	private int status;

	@ManyToOne
	@JoinColumn(name = "customerId")
	private Customer customer;

	@OneToMany(mappedBy = "order")
	private Collection<OrderDetail> orderDetails;

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", phone=" + phone + ", orderDate=" + orderDate + ", requireDate="
				+ requireDate + ", receiver=" + receiver + ", address=" + address + ", description=" + description
				+ ", amount=" + amount + ", totalPrice=" + totalPrice + ", customer=" + customer + ", orderDetails="
				+ orderDetails + "]";
	}

	public Order(Integer orderId, String phone, Date orderDate, Date requireDate, String receiver, String address,
			String description, Double amount, Double totalPrice, Customer customer,
			Collection<OrderDetail> orderDetails, int status) {
		super();
		this.orderId = orderId;
		this.phone = phone;
		this.orderDate = orderDate;
		this.requireDate = requireDate;
		this.receiver = receiver;
		this.address = address;
		this.description = description;
		this.amount = amount;
		this.totalPrice = totalPrice;
		this.customer = customer;
		this.orderDetails = orderDetails;
		this.status = status;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getRequireDate() {
		return requireDate;
	}

	public void setRequireDate(Date requireDate) {
		this.requireDate = requireDate;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Collection<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Collection<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Order() {
		super();
	}

}
