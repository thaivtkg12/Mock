package com.java.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "orderdetails")
public class OrderDetail implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderDetailId;
	private Double price;
	private Integer quantity;
	private Double discount;
	private String status;
	@Column(name = "total_price")
	private double totalPrice;
	@ManyToOne
	@JoinColumn(name = "orderId")
	private Order order;
	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public OrderDetail(Integer orderDetailId, Double price, Integer quantity, Double discount, String status,
			double totalPrice, Order order, Product product) {
		super();
		this.orderDetailId = orderDetailId;
		this.price = price;
		this.quantity = quantity;
		this.discount = discount;
		this.status = status;
		this.totalPrice = totalPrice;
		this.order = order;
		this.product = product;
	}

	public OrderDetail() {
		super();
	}

}
