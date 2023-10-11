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
import com.ze.pigSale.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author z
 * @description 针对表【orders】的数据库操作Mapper
 * @createDate 2023-04-04 20:21:04
 * @Entity com.ze.pigSale.entity.Orders
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 查询范围时间内的所有订单号
     *
     * @return
     */
    List<Orders> listByTime(@Param("ordersId") Long ordersId, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    List<Orders> list();

    /**
     * 根据id获取订单
     * @param id
     * @return
     */
    Orders getById(@Param("id") Long id);

    /**
     * 提交订单
     *
     * @param orders
     */
    void save(Orders orders);

    /**
     * 查看用户的所有订单
     *
     * @return
     */
    List<Orders> getListByUserId(@Param("userId") Long userId, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 修改订单
     * @param orders
     */
    void updateOrdersById(Orders orders);

    /**
     * 获取已经送达但是没有评价的订单
     * @param userId
     * @return
     */
    List<Orders> getByNoReview(@Param("userId") Long userId);

    /**
     * 获取指定时间内的订单数量
     * @param start
     * @param end
     * @return
     */
    Integer getCountByTime(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
