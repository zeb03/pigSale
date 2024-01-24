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

    public static final String INSERT_QUEUE_NAME = "product.insert.queue";
    public static final String DELETE_QUEUE_NAME = "product.delete.queue";
    public static final String EXCHANGE_NAME = "product.topic";
    public static final String DELETE_KEY = "product.delete";
    public static final String INSERT_KEY = "product.insert";
    public static final String ORDER_QUEUE_NAME = "order.queue";
    public static final String CANAL_STOCK_NAME = "pigsale-canal-product-stock";
    public static final String CANAL_CONSUMER_GROUP = "canal-group";

}
