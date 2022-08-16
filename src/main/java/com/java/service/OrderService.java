package com.java.service;

import java.util.List;

import com.java.entity.Order;

public interface OrderService {
	Order getOrderByID(int id);
	List<Order> getAllOrder();
	boolean checkExistsOrderID(int id);
	void updateOrder(Order order);
	Order findByOrderIdAndEmail(Integer orderId,String customerId);
}
