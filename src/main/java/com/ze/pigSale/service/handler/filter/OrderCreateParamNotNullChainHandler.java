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

package com.ze.pigSale.service.handler.filter;

import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.dto.OrdersDTO;
import com.ze.pigSale.service.handler.OrderCreateChainHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 验证参数非空
 * @author zeb
 * @Date 2024-01-21 15:48
 */
@Slf4j
@Component
public class OrderCreateParamNotNullChainHandler implements OrderCreateChainHandler<OrdersDTO> {

    @Override
    public void handle(OrdersDTO requestParam) {
        log.info("开始验证参数是否为空");
        Long addressId = requestParam.getAddressId();
        if (addressId == null) {
            throw new CustomException("请选择地址");
        }
        // 获取购买的商品id
        List<Long> cartItemIds = requestParam.getCartItemIds();
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new CustomException("请选择商品");
        }

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
