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

import com.ze.pigSale.entity.OrderDetail;
import com.ze.pigSale.vo.OrderDetailVo;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-05-20:33
 */

public interface OrderDetailService {

    /**
     * 批量保存
     * @param orderDetails
     */
    void saveBatch(List<OrderDetail> orderDetails);

    /**
     * 获取某订单详情
     * @param OrderId
     * @return
     */
    List<OrderDetail> getListByOrderId(Long OrderId);

    /**
     * 获取已经送达但是没有评价的订单
     * @param userId
     * @return
     */
    List<OrderDetailVo> getByNoReview(Long userId);
}
