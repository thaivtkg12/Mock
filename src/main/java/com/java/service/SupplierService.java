package com.java.service;

import java.util.List;
import java.util.Optional;

import com.java.entity.Supplier;

public interface SupplierService {
	List<Supplier> findAll();
	
	Optional<Supplier> findById(Integer id);
	
	Supplier save(Supplier supplier);
}
