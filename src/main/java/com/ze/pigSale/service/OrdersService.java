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

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.dto.OrdersDTO;
import com.ze.pigSale.entity.Orders;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * author: zebii
 * Date: 2023-04-04-20:29
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 根据id获取订单
     *
     * @param ordersId
     * @return
     */
    Orders getById(Long ordersId);

    /**
     * 管理员获取订单信息
     * 可以模糊查询订单号，按指定时间范围进行修改
     * @param currentPage
     * @param pageSize
     * @param ordersId
     * @param beginTime
     * @param endTime
     * @return
     */
    PageInfo<OrdersDTO> getPageWithDetail(int currentPage, int pageSize, Long ordersId, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 提交订单
     *
     * @param ordersDto
     */
    void submit(OrdersDTO ordersDto);

    /**
     * 查看用户的所有订单
     * @param currentPage
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @return
     */
    PageInfo<OrdersDTO> getListByUserId(int currentPage, int pageSize, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 修改订单
     *
     * @param orders
     */
    void updateStatus(Orders orders);

    /**
     * 再来一单
     *
     * @param orders
     */
    void again(Orders orders);

    /**
     * 修改订单
     *
     * @param orders
     */
    void updateOrdersById(Orders orders);

    /**
     * 同意退款
     *
     * @param orders
     */
    void agree(Orders orders);

    /**
     * 拒接退款
     * @param orders
     * @param request
     */
    void disagree(Orders orders, HttpServletRequest request);

    /**
     * 获取指定时间内的订单数量
     * @param start
     * @param end
     * @return
     */
    Integer getCountByTime(LocalDateTime start, LocalDateTime end);

    /**
     * 用户申请退款
     * @param ordersId
     * @param request
     * @return
     */
    Result<String> cancelOrders(Long ordersId, HttpServletRequest request);

    /**
     * 订单超时关闭
     * @param ordersDTO
     * @return
     */
    boolean closeOrders(OrdersDTO ordersDTO);

    /**
     * 用户支付
     * @param orders
     */
    void pay(Orders orders);
}