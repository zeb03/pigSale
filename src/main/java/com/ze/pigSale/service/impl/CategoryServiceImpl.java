/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ze.pigSale.service.impl;

import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.entity.Category;
import com.ze.pigSale.entity.User;
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
        Category category = categoryMapper.getCategoryById(categoryId);
        if (category == null) {
            throw new CustomException("该种类不存在");
        }
        categoryMapper.deleteCategory(categoryId);
    }
}
