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
 * @Date 2023-08-07 21:43
 */
public class EsConstants {

    public static final String SEARCH_INDEX_REQUEST = "pig_product";
    public static final String SEARCH_KEYWORDS = "all";
    public static final String CATEGORY_AGGREGATION = "categoryAgg";
    public static final String ORIGIN_AGGREGATION = "originAgg";
    public static final Integer AGGREGATION_SIZE = 3;
    public static final String ORIGIN_FIELD = "origin";
    public static final String CATEGORY_FIELD = "categoryName";
    public static final String PRODUCT_SUGGEST = "productSuggest";
    public static final String SUGGESTION_FIELD = "suggestion";
    public static final Integer SUGGESTION_SIZE = 10;

}
