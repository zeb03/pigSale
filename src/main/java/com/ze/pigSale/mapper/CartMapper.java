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
import com.ze.pigSale.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【cart】的数据库操作Mapper
* @createDate 2023-03-13 15:33:54
* @Entity com.ze.pig.entity.Cart
*/
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 根据用户id，产品id获取购物车
     * @param cart
     * @return
     */
    Cart getCart(Cart cart);

    /**
     * 根据id获取对应的购物车项
     * @param cartItemIds
     * @return
     */
    List<Cart> ListByIds(@Param("cartItemIds") List<Long> cartItemIds);

    /**
     * 根据用户id获取购物车
     * @param userId
     * @return
     */
    List<Cart> listByUserId(@Param("userId") Long userId);

    /**
     * 保存购物车
     * @param cart
     */
    void save(Cart cart);

    /**
     * 清空购物车
     * @param userId
     */
    void deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据id删除
     * @param cart
     */
    void deleteById(Cart cart);

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(@Param("ids") List<Long> ids);

    void updateCartById(Cart cart);
}
