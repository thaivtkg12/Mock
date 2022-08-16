package com.java.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	// Hiển thị Top 10 sách bán chạy nhất
	@Query(value = "SELECT p.product_id,\r\n"
			+ "  COUNT(*) AS SoLuong\r\n"
			+ "FROM orderdetails p\r\n"
			+ "JOIN products c ON p.product_id = c.product_id\r\n"
			+ "GROUP BY p.product_id\r\n"
			+ "ORDER by SoLuong DESC limit 10;", nativeQuery = true)
	public List<Object[]> topSellingProduct10();
	

	@Query(value = "select * from products o where product_id in :ids", nativeQuery = true)
	List<Product> findByInventoryIds(@Param("ids") List<Integer> listproduct_id);
	
	// Cart Item
	@Query(value = "SELECT * FROM products where product_id = ?" , nativeQuery = true)
    public Product findByIdProduct (int product_id);

	// Hiển thị danh sách product mới nhất ở trang chủ LIMIT = 10
	@Query(value = "SELECT * FROM products ORDER BY entered_date DESC limit 10", nativeQuery = true)
	public List<Product> listProduct10();
	
	
	@Query(value = "SELECT * FROM products ", nativeQuery = true)
	public List<Product> listallProduct();

	// List Sản phẩm by danh mục
	@Query(value = "SELECT * FROM products WHERE category_id = ?", nativeQuery = true)
	public List<Product> listProductByCategory(Integer categoryId);
	
	@Query(value = "select * from products where category_id = ?", nativeQuery = true)
	Page<Product> findAllProductByCategoryId(Integer id, Pageable pageable);

	// List Sản phẩm by nhà cung cấp
	@Query(value = "SELECT * FROM products where supplier_id = ?", nativeQuery = true)
	public List<Product> listProductBySupplier(Integer supplierId);

	// Search Product
	@Query(value = "SELECT * FROM products WHERE name LIKE %?1% OR description LIKE %?1%", nativeQuery = true)
	public List<Product> searchProduct(String name);
	
	// Gợi ý sản phẩm cùng thể loại
	@Query(value = "SELECT \r\n"
			+ "*FROM products AS p\r\n"
			+ "WHERE p.category_id = ?;" , nativeQuery = true)
	List<Product> productsByCategory(Integer categoryId);

}
