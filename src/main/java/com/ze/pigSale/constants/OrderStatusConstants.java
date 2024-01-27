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

package com.ze.pigSale.constants;

/**
 * @author zeb
 * @Date 2023-07-29 13:36
 */
public class OrderStatusConstants {
    /**
     * 订单未支付
     */
    public static final Integer ORDER_NOT_PAY = 1;
    /**
     * 订单已支付，待发货
     */
    public static final Integer ORDER_HAD_PAY = 2;
    /**
     * 订单已发货
     */
    public static final Integer ORDER_HAD_SEND = 3;
    /**
     * 订单已到达
     */
    public static final Integer ORDER_HAD_ARRIVAL = 4;
    /**
     * 订单申请退款
     */
    public static final Integer ORDER_APPLY_CANCEL = 5;
    /**
     * 订单已经退款、关闭
     */
    public static final Integer ORDER_HAS_CANCEL = 6;
}
