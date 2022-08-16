package com.java.entity;



public class CartItemWish {

	private int productId;
	private String name;
	private double unitPrice;
	private Product product;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public CartItemWish() {
		super();
	}

	public CartItemWish(int productId, String name, double unitPrice, Product product) {
		super();
		this.productId = productId;
		this.name = name;
		this.unitPrice = unitPrice;
		this.product = product;
	}

}
