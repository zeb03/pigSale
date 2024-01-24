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

    public static final String PRODUCT_NAME = "product_topic";
    public static final String PRODUCT_CON_GROUP = "product_con_group";

    public static final String CANAL_STOCK_NAME = "canal_product_stock";
    public static final String CANAL_CON_GROUP = "canal_group";

    public static final String ORDER_NAME = "order_queue";
    public static final String ORDER_CON_GROUP = "order_con_group";

}
