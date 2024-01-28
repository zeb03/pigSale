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

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.anno.PermissionAnno;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.vo.ProductVo;
import com.ze.pigSale.entity.Category;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.service.CategoryService;
import com.ze.pigSale.service.ProductService;
import com.ze.pigSale.vo.SalesVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ze.pigSale.enums.PermissionEnum.*;

/**
 * @author: zebii
 * Date: 2023-03-15-19:53
 */
@Slf4j
@RestController()
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;
    private CategoryService categoryService;

    /**
     * 收藏商品
     *
     * @param productId
     * @return
     */
    @PutMapping("/collect/{productId}")
    public Result collectProduct(@PathVariable("productId") Long productId) {
        return productService.collectProduct(productId);
    }

    /**
     * 获取收藏列表
     *
     * @return
     */
    @GetMapping("/collections")
    public Result getCollections() {
        return productService.getCollections();
    }

    /**
     * 查看商品详情
     *
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public Result detail(@PathVariable("productId") Long productId) {
        return productService.detail(productId);
    }

    /**
     * 分页查询所有产品
     *
     * @param currentPage
     * @param pageSize
     * @param keyword
     * @param categoryId
     */
    @GetMapping("/page")
    public Result<PageInfo<ProductVo>> page(Integer currentPage, Integer pageSize, String keyword, Long categoryId) {
        return productService.getPage(currentPage, pageSize, keyword, categoryId);
    }

    /**
     * 新增产品
     *
     * @param product
     */
    @PostMapping
    @PermissionAnno(value = ADD_PRODUCT)
    public Result<Product> add(@RequestBody Product product) {
        productService.insertProduct(product);
        return Result.success(product);
    }

    /**
     * 修改产品
     *
     * @param product
     */
    @PutMapping
    @PermissionAnno(value = EDIT_PRODUCT)
    public Result<Product> edit(@RequestBody Product product) {
        productService.updateProduct(product);
        return Result.success(product);
    }

    /**
     * 移除商品
     *
     * @param productId
     */
    @DeleteMapping("/{productId}")
    @PermissionAnno(value = DELETE_PRODUCT)
    public Result<String> delete(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return Result.success("删除成功");
    }

    /**
     * 获取最近销量
     */
    @GetMapping("/salesRank")
    @PermissionAnno(value = VIEW_DATA)
    public Result<SalesVO> getSalesRank(Integer month) {

        Map<String, Integer> salesRank = productService.getSalesRank(month);
        Set<String> keySet = salesRank.keySet();
        Collection<Integer> values = salesRank.values();

        SalesVO salesVO = new SalesVO();
        salesVO.setProductNames(keySet);

        salesVO.setSales(values);

        return Result.success(salesVO);
    }

    /**
     * 获取最近收益
     */
    @GetMapping("/benefit/all")
    @PermissionAnno(value = VIEW_DATA)
    public Result<Map<String, BigDecimal>> getAllBenefit(Integer month) {
        Map<String, BigDecimal> benefit = productService.getAllBenefit(month);
        return Result.success(benefit);
    }

    /**
     * 获取最近一年总收益
     */
    @GetMapping("/benefit")
    @PermissionAnno(value = VIEW_DATA)
    public Result<List<BigDecimal>> getBenefit() {
        List<BigDecimal> benefit = productService.getBenefit();
        return Result.success(benefit);
    }

    /**
     * 获取指定时间内订单成交量
     */
    @GetMapping("/thisYearOrders")
    @PermissionAnno(value = VIEW_DATA)
    public Result<List<Integer>> getOrderCount() {
        List<Integer> orderCount = productService.getOrderCount();
        return Result.success(orderCount);
    }
}
