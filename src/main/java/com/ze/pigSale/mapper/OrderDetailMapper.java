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

import com.ze.pigSale.dto.OrderDetailDTO;
import com.ze.pigSale.entity.OrderDetail;
import com.ze.pigSale.vo.OrderDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author z
* @description 针对表【order_detail】的数据库操作Mapper
* @createDate 2023-04-04 20:23:19
* @Entity com.ze.pigSale.entity.OrderDetail
*/
@Mapper
public interface OrderDetailMapper {

    /**
     * 保存订单详情
     * @param orderDetail
     */
    void save(OrderDetail orderDetail);

    /**
     * 批量保存
     * @param orderDetails
     */
    void saveBatch(List<OrderDetail> orderDetails);

    /**
     * 获取某订单详情
     * @param orderId
     * @return
     */
    List<OrderDetail> getListByOrderId(@Param("orderId") Long orderId);

    /**
     * 获取已经送达但是没有评价的订单
     * @param userId
     * @return
     */
    List<OrderDetailVo> getByNoReview(@Param("userId") Long userId);

    /**
     * 获取最近的销量
     */
    List<OrderDetailDTO> getListByTime(@Param("now") LocalDateTime now, @Param("time") LocalDateTime time);

}
