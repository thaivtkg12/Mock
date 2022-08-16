package com.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.entity.Product;
import com.java.repository.ProductRepository;

public interface ProductService {
	public List<Object[]> topSellingProduct10();

	List<Product> findByInventoryIds(List<Integer> listproduct_id);

// Cart Item

	public Product findByIdProduct(int product_id);

// Hiển thị danh sách product mới nhất ở trang chủ LIMIT = 10

	public List<Product> listProduct10();

	public List<Product> listallProduct();

// List Sản phẩm by danh mục

	public List<Product> listProductByCategory(Integer categoryId);

	Page<Product> findAllProductByCategoryId(Integer id, Pageable pageable);

// List Sản phẩm by nhà cung cấp

	public List<Product> listProductBySupplier(Integer supplierId);

// Search Product

	public List<Product> searchProduct(String name);

	List<Product> productsByCategory(Integer categoryId);

	Product save(Product product);
	
	List<Product> findAll();
	
	void deleteById(Integer Id);
	
	Optional<Product> findById(Integer id);

}
