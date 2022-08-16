package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	
	@Query(value = "select * from orders where customer_id = ?1", nativeQuery = true)
	List<Order> findByCustomerId(String id);
	
	// List những đơn hàng chưa thanh toán cho 
	@Query(value = "select o1.orderId, o1.receiver,o1.address, o1.phone, o1.total_price,o2.status\r\n"
			+ "from orders o1\r\n"
			+ "inner join orderdetails o2\r\n"
			+ "on o1.orderId = o2.orderId\r\n"
			+ "where o2.status = 'Đang Chờ Xử Lý'\r\n"
			+ "GROUP BY o1.orderId, o1.receiver,o1.address, o1.phone, o1.total_price,o2.status", nativeQuery = true)
	
	List<Object[]> listforship();
	
	
	   @Query(value = "Select sum(total_price)\r\n"
		   		+ "from orders\r\n"
		   		+ "where status = '3'", nativeQuery = true)
		   public List<Integer> doanhthuDetails();
	boolean existsOrderByOrderId(int id);
	@Query("SELECT o from Order o where o.orderId=?1 and o.customer.email=?2")
	Order findByOrderIdAndCustomerId(Integer orderId,String email);

}
