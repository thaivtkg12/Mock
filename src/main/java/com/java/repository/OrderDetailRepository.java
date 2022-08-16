package com.java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.entity.Order;
import com.java.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
	
	@Query(value = "select * from orderdetails where order_id = ?", nativeQuery = true)
	List<OrderDetail> findByOrderId(int id);
	
	// thống kê theo sản phẩm được bán ra
    @Query(value = "SELECT p.name ,  \r\n"
    		+ "SUM(o.quantity) as quantity ,\r\n"
    		+ "SUM(o.quantity * o.total_price) as sum,\r\n"
    		+ "AVG(o.total_price) as avg,\r\n"
    		+ "Min(o.total_price) as min, \r\n"
    		+ "max(o.total_price) as max\r\n"
    		+ "FROM orderdetails o\r\n"
    		+ "INNER JOIN products p ON o.product_id = p.product_id\r\n"
    		+ "GROUP BY p.name;", nativeQuery = true)

    public List<Object[]> repo();
    
    // Thống kê theo thể loại được bán ra
    @Query(value = "SELECT c.name , \r\n"
    		+ "SUM(o.quantity) as quantity ,\r\n"
    		+ "SUM(o.quantity * o.total_price) as sum,\r\n"
    		+ "AVG(o.total_price) as avg, \r\n"
    		+ "Min(o.total_price) as min,\r\n"
    		+ "max(o.total_price) as max \r\n"
    		+ "FROM orderdetails o\r\n"
    		+ "INNER JOIN products p ON o.product_id = p.product_id\r\n"
    		+ "INNER JOIN categories c ON p.categoryId = c.categoryId\r\n"
    		+ "GROUP BY c.name;", nativeQuery = true)

    public List<Object[]> repoWhereCategory();
    
    // Thống kê các sp từ nhà cung cấp được bán ra
    @Query(value = "SELECT s.name , \r\n"
    		+ "SUM(o.quantity) as quantity ,\r\n"
    		+ "SUM(o.quantity * o.total_price) as sum,\r\n"
    		+ "AVG(o.total_price) as avg  ,\r\n"
    		+ "Min(o.total_price) as min  ,\r\n"
    		+ "max(o.total_price) as max \r\n"
    		+ "FROM orderdetails o\r\n"
    		+ "INNER JOIN products p ON o.product_id = p.product_id\r\n"
    		+ "INNER JOIN suppliers s ON p.supplierId = s.id\r\n"
    		+ "GROUP BY s.name;", nativeQuery = true)

    public List<Object[]> repoWhereSuppliers();
    
    // Thống kê sản phẩm theo năm // theo các năm
    @Query(value = "Select YEAR(od.order_date) ,\r\n"
    		+ "SUM(o.quantity) as quantity ,\r\n"
    		+ "SUM(o.quantity * o.total_price) as sum,\r\n"
    		+ "AVG(o.total_price) as avg  ,\r\n"
    		+ "Min(o.total_price) as min  ,\r\n"
    		+ "max(o.total_price) as max \r\n"
    		+ "FROM orderdetails o\r\n"
    		+ "INNER JOIN orders od ON o.order_id =od.order_id\r\n"
    		+ "GROUP BY YEAR(od.order_date);", nativeQuery = true)
    public List<Object[]> repoWhereYear();
    
    // Thống kê sản phẩm theo tháng // theo các Tháng
    @Query(value = "Select month(od.order_date) ,\r\n"
    		+ "SUM(o.quantity) as quantity ,    \r\n"
    		+ "SUM(o.quantity * o.total_price) as sum,\r\n"
    		+ "AVG(o.total_price) as avg  ,\r\n"
    		+ "Min(o.total_price) as min  ,\r\n"
    		+ "max(o.total_price) as max\r\n"
    		+ "FROM orderdetails o\r\n"
    		+ "INNER JOIN orders od ON o.order_id =od.order_id\r\n"
    		+ "GROUP BY month(od.order_date);", nativeQuery = true)

    public List<Object[]> repoWhereMonth();
    
    // Thống kê sản phẩm theo quý // theo các quý
    @Query(value = "Select QUARTER(od.order_date),\r\n"
    		+ "SUM(o.quantity) as quantity , \r\n"
    		+ "SUM(o.quantity * o.total_price) as sum,\r\n"
    		+ "AVG(o.total_price) as avg, \r\n"
    		+ "Min(o.total_price) as min,\r\n"
    		+ "max(o.total_price) as max\r\n"
    		+ "FROM orderdetails o\r\n"
    		+ "INNER JOIN orders od ON o.order_id =od.order_id\r\n"
    		+ "GROUP By QUARTER(od.order_date);", nativeQuery = true)

    public List<Object[]> repoWhereQUARTER();
    
    // Thống kê sản phẩm theo người đặt hàng
   @Query(value = "SELECT c.customer_id,\r\n"
   		+ "SUM(o.quantity) as quantity,  \r\n"
   		+ "SUM(o.quantity * o.total_price) as sum,\r\n"
   		+ "AVG(o.total_price) as avg,\r\n"
   		+ "Min(o.total_price) as min, \r\n"
   		+ "max(o.total_price) as max \r\n"
   		+ "FROM orderdetails o\r\n"
   		+ "INNER JOIN orders p ON o.order_id = p.order_id\r\n"
   		+ "INNER JOIN customers c ON p.customer_id = c.customer_id\r\n"
   		+ "GROUP BY c.customer_id;", nativeQuery = true)
   public List<Object[]> reportCustommer();
   
   
   @Query(value = "Select sum(total_price)\r\n"
   		+ "from orderdetails\r\n"
   		+ "where status = 'Đã Thanh Toán'", nativeQuery = true)
   public List<Integer> doanhthuDetails();
   
   
   @Query(value = "Select count(status)\r\n"
   		+ "from orderdetails\r\n"
   		+ "where status = 'Đã Thanh Toán'", nativeQuery = true)
	   public List<Integer> succeesOder();
   
   
   @Query(value = "Select count(status)\r\n"
	   		+ "from orderdetails\r\n"
    		+ "where status = 'Đang Chờ Xử Lý'", nativeQuery = true)
		   public List<Integer> comingOder();
   
   
	@Query(value = "update orderdetails\r\n"
			+ "set status = 'Đã Thanh Toán'\r\n"
			+ "where order_id = ?", nativeQuery = true)
	Optional<OrderDetail> updateOrder(int id);
	
	@Query(value = "select * from orderdetails where orderdetail_id = ?", nativeQuery = true)
	Optional<OrderDetail> findbyId(int id);
	
	@Query("SELECT o FROM OrderDetail o WHERE o.order = ?1 ")
	List<OrderDetail> findOrderDetailsByOrderID(Order order);
   

}
