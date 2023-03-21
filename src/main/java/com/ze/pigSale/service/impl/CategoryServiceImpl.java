package com.ze.pigSale.service.impl;

import com.ze.pigSale.entity.Category;
import com.ze.pigSale.mapper.CategoryMapper;
import com.ze.pigSale.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-15-22:46
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category getCategoryById(Long id) {
        return categoryMapper.getCategoryById(id);
    }

    @Override
    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    @Override
    public void insertCategory(Category category) {
        categoryMapper.insertCategory(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.updateCategory(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryMapper.deleteCategory(categoryId);
    }
}
