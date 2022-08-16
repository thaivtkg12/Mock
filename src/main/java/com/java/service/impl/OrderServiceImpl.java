package com.java.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.java.entity.Order;
import com.java.repository.OrderRepository;
import com.java.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public List<Order> getAllOrder() {
		return orderRepository.findAll(Sort.by("status").ascending());
	}

	@Override
	public boolean checkExistsOrderID(int id) {
		return orderRepository.existsOrderByOrderId(id);
	}

	@Override
	public Order getOrderByID(int id) {
		return orderRepository.findById(id).get();
	}

	@Override
	public void updateOrder(Order order) {
		orderRepository.save(order);
	}

	@Override
	public Order findByOrderIdAndEmail(Integer orderId, String customerId) {
		// TODO Auto-generated method stub
		return orderRepository.findByOrderIdAndCustomerId(orderId, customerId);
	}

}
