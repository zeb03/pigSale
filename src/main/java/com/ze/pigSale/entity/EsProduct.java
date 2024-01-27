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

package com.ze.pigSale.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zeb
 * @Date 2023-08-07 15:10
 */
@Data
@EqualsAndHashCode
@Document(indexName = "pig_product")
@Setting(settingPath = "analyzer.json")
public class EsProduct implements Serializable {

    private static final long serialVersionUID = -1L;
    @Id
    private Long productId;

    @Field(analyzer = "text_analyzer", searchAnalyzer = "ik_max_word", type = FieldType.Text, copyTo = "all")
    private String productName;

    @Field(analyzer = "text_analyzer", searchAnalyzer = "ik_max_word", type = FieldType.Text, copyTo = "all")
    private String description;

    private Long categoryId;

    @Field(type = FieldType.Keyword, copyTo = "all")
    private String categoryName;

    private BigDecimal price;

    @Field(type = FieldType.Keyword, index = false)
    private String image;

    @Field(type = FieldType.Keyword)
    private String origin;

    @Field(type = FieldType.Integer)
    private Integer sales;

    @Field(type = FieldType.Integer)
    private Integer stock;

    @CompletionField(analyzer = "completion_analyzer")
    private Completion suggestion;

    public EsProduct() {
    }

    public EsProduct(Product product) {

    }
    // @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd HH:mm:ss")
    // private LocalDateTime createTime;
    //
    // @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd HH:mm:ss")
    // private LocalDateTime updateTime;
}
