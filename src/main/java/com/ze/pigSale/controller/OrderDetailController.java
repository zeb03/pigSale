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

package com.ze.pigSale.controller;

import com.ze.pigSale.common.Result;
import com.ze.pigSale.service.OrderDetailService;
import com.ze.pigSale.vo.OrderDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: ze
 * @Date: 2023-04-19-13:26
 */
@RestController
@Slf4j
@RequestMapping("/order/detail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 获取用户未评价订单
     * @param userId
     * @return
     */
    @GetMapping("/review/{userId}")
    public Result<List<OrderDetailVo>> listWithNoReview(@PathVariable("userId") Long userId) {
        List<OrderDetailVo> orderDetailVos = orderDetailService.getByNoReview(userId);
        return Result.success(orderDetailVos);
    }
}
