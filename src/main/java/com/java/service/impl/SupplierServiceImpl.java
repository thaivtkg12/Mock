package com.java.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.entity.Supplier;
import com.java.repository.SuppliersRepository;
import com.java.service.SupplierService;

@Service
public class SupplierServiceImpl implements SupplierService  {
	
	@Autowired
	SuppliersRepository suppliersRepository;

	@Override
	public List<Supplier> findAll() {
		// TODO Auto-generated method stub
		return suppliersRepository.findAll();
	}

	@Override
	public Optional<Supplier> findById(Integer id) {
		// TODO Auto-generated method stub
		return suppliersRepository.findById(id);
	}

	@Override
	public Supplier save(Supplier supplier) {
		// TODO Auto-generated method stub
		return suppliersRepository.save(supplier);
	}
	
}
