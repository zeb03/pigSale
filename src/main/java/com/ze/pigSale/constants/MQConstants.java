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
 * @Date 2023-08-07 22:31
 */
public class MQConstants {

    public static final String DELETE = "delete";
    public static final String INSERT = "insert";

    /**
     * ES数据一致性TOPIC
     */
    public static final String PRODUCT_ES_SYNC_TOPIC_KEY = "pigsale_product-service_sync-product_topic";

    /**
     * ES数据一致性消费者组
     */
    public static final String PRODUCT_ES_SYNC_CON_GROUP = "pigsale_product-service_sync-product_con_group";

    /**
     * Canal订阅binlog的TOPIC
     */
    public static final String CANAL_STOCK_TOPIC_KEY = "pigsale_order-service_canal_product_stock";

    /**
     * Canal订阅binlog的消费者组
     */
    public static final String CANAL_STOCK_CON_GROUP = "pigsale_order-service_canal_group";

    /**
     * 订单异步提交TOPIC
     */
    public static final String ORDER_ASYNC_SUBMIT_TOPIC_KEY = "pigsale_order-service_submit-order_topic";

    /**
     * 订单异步提交消费者组
     */
    public static final String ORDER_CON_GROUP = "pigsale_order-service_order_con_group";

    /**
     * 订单延迟关闭TOPIC
     */
    public static final String ORDER_DELAY_CLOSE_TOPIC_KEY = "pigsale_order-service_delay-close-order_topic";

    /**
     * 订单延迟关闭消费者组
     */
    public static final String TICKET_DELAY_CLOSE_CG_KEY = "pigsale_order-service_delay-close-order_con_group";

}
