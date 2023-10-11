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

import com.ze.pigSale.common.CommonPage;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.EsProduct;
import com.ze.pigSale.service.EsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zeb
 * @Date 2023-08-07 15:34
 */
@RestController()
@Api(tags = "EsProductController", description = "搜索商品管理")
@RequestMapping("/es/product")
public class EsProductController {

    @Autowired
    private EsProductService esProductService;

    @ApiOperation(value = "导入所有数据库中商品到ES")
    @PostMapping("/importAll")
    public Result<Integer> importAllList() {
        int count = esProductService.importAll();
        return Result.success(count);
    }

    @ApiOperation(value = "简单搜索")
    @GetMapping("/search/simple")
    @ResponseBody
    public Result search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                         @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsProduct> esProductPage = esProductService.search(keyword, pageNum, pageSize);
        return Result.success(CommonPage.restPage(esProductPage));
    }

    @ApiOperation(value = "自动补全")
    @GetMapping("/suggest")
    public Result<List> suggest(String prefix) {
        return esProductService.suggest(prefix);
    }

    @ApiOperation(value = "数据聚合")
    @GetMapping("/aggregate")
    public Result<Map<String, List>> aggregate(@RequestParam(required = false) String keyword,
                                               @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                               @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return esProductService.aggregate(keyword, pageNum, pageSize);
    }

}
