package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	@Query(value = "SELECT * FROM categories", nativeQuery = true)
	 public List<Category> listCategories ();

}
