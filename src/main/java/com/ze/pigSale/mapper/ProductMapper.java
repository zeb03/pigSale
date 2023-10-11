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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ze.pigSale.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author z
* @description 针对表【product】的数据库操作Mapper
* @createDate 2023-03-15 19:52:59
* @Entity com.ze.pigSale.entity.Product
*/
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 根据id查找产品
     * @param productId
     * @return
     */
    Product getProductById(@Param("productId") Long productId);

    /* 查询所有产品 */
    List<Product> getAllProduct(@Param("productName") String productName, @Param("categoryId") Long categoryId);

    /* 根据名字查询所有产品 */
    List<Product> getAllProductByName(@Param("name") String name);

    /**
     * 添加商品
     * @param product
     */
    void insertProduct(Product product);

    /**
     * 修改商品
     * @param product
     */
    void updateProduct(Product product);

    /**
     * 删除商品
     * @param productId
     */
    void deleteProduct(@Param("productId") Long productId);
}
