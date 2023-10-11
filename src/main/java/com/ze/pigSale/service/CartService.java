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

package com.ze.pigSale.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ze.pigSale.entity.Cart;
import com.ze.pigSale.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-31-16:39
 */
public interface CartService extends IService<Cart> {

    /**
     * 根据用户id，产品id获取购物车
     * @param productId
     * @return
     */
    Cart getCart(Long productId);

    /**
     * 根据用户id获取购物车
     * @param userId
     * @return
     */
    List<Cart> getCartListByUser(Long userId);

    /**
     * 根据id获取对应的购物车项
     * @param cartItemIds
     * @return
     */
    List<Cart> getCartListByIds(List<Long> cartItemIds);

    /**
     * 根据id修改
     * @param cart
     */
    void updateCartById(Cart cart);

    /**
     * 添加购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 清空购物车
     * @param userId
     */
    void cleanCart(Long userId);

    /**
     * 删除购物车
     * @param cart
     */
    void deleteCart(Cart cart);

    /**
     * 删除购物车
     * @param ids
     */
    void deleteBatch(List<Long> ids);

}
