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

package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【category】的数据库操作Mapper
* @createDate 2023-03-15 19:52:59
* @Entity com.ze.pigSale.entity.Category
*/
@Mapper
public interface CategoryMapper {

    /* 根据id获取category */
    Category getCategoryById(@Param("id") Long id);

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
    void deleteCategory(@Param("categoryId") Long categoryId);
}
