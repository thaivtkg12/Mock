package com.java.service;

import java.util.List;

import com.java.entity.Category;

public interface CategoryService {
    Category findByCategoryId(Integer id);
    
    Category save(Category category);
    
    List<Category> findAll();
}
