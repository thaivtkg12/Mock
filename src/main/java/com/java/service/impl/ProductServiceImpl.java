package com.java.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.java.entity.Product;
import com.java.repository.ProductRepository;
import com.java.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository productRepository;

	@Override
	public List<Object[]> topSellingProduct10() {
		// TODO Auto-generated method stub
		return productRepository.topSellingProduct10();
	}

	@Override
	public List<Product> findByInventoryIds(List<Integer> listproduct_id) {
		// TODO Auto-generated method stub
		return productRepository.findByInventoryIds(listproduct_id);
	}

	@Override
	public Product findByIdProduct(int product_id) {
		// TODO Auto-generated method stub
		return productRepository.findByIdProduct(product_id);
	}

	@Override
	public List<Product> listProduct10() {
		// TODO Auto-generated method stub
		return productRepository.listProduct10();
	}

	@Override
	public List<Product> listallProduct() {
		// TODO Auto-generated method stub
		return productRepository.listallProduct();
	}

	@Override
	public List<Product> listProductByCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return productRepository.listProductByCategory(categoryId);
	}

	@Override
	public Page<Product> findAllProductByCategoryId(Integer id, Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findAllProductByCategoryId(id, pageable);
	}

	@Override
	public List<Product> listProductBySupplier(Integer supplierId) {
		// TODO Auto-generated method stub
		return productRepository.listProductBySupplier(supplierId);
	}

	@Override
	public List<Product> searchProduct(String name) {
		// TODO Auto-generated method stub
		return productRepository.searchProduct(name);
	}

	@Override
	public List<Product> productsByCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return productRepository.productsByCategory(categoryId);
	}

	@Override
	public Product save(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public void deleteById(Integer Id) {
		// TODO Auto-generated method stub
		productRepository.deleteById(Id);
	}

	@Override
	public Optional<Product> findById(Integer id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id);
	}

	
}
