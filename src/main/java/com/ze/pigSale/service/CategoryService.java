package com.ze.pigSale.service;

import com.ze.pigSale.entity.Category;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-15-22:43
 */
public interface CategoryService {
    /* 根据id获取category*/
    Category getCategoryById(Long id);

    /**
     * 获取所有的种类
     * @return
     */
    List<Category> getCategoryList();

    /**
     * 插入种类
     */
    void insertCategory(Category category);

    /**
     * 修改种类
     * @param category
     */
    void updateCategory(Category category);

    /**
     * 删除种类
     * @param categoryId
     */
    void deleteCategory(Long categoryId);
}
