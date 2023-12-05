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

package com.ze.pigSale;

import cn.hutool.core.bean.BeanUtil;
import com.ze.pigSale.entity.Category;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.entity.Review;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.service.CategoryService;
import com.ze.pigSale.service.ProductService;
import com.ze.pigSale.service.ReviewService;
import com.ze.pigSale.service.UserService;
import com.ze.pigSale.utils.CacheClient;
import com.ze.pigSale.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ze.pigSale.constants.RedisConstants.CACHE_SHOP_KEY;
import static com.ze.pigSale.constants.RedisConstants.PRODUCT_STOCK_KEY;

@Slf4j
@SpringBootTest
class PigSaleApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    @Resource
    private CacheClient cacheClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("test.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @Test
    void contextLoads() {
        User user = userService.getUserById(1L);
        log.info("{}", user);
    }

    @Test
    void testSaveShop() throws InterruptedException {
        Product product = productService.getProductById(9L);
        ProductVo productVo = BeanUtil.copyProperties(product, ProductVo.class);
        Category category = categoryService.getCategoryById(productVo.getCategoryId());
        if (category != null) {
            productVo.setCategoryName(category.getCategoryName());
        }

        // 获取商品评分
        List<Review> reviews = reviewService.getListByProduct(product.getProductId());
        if (reviews == null || reviews.isEmpty()) {
            productVo.setRating(0.0);
        } else {
            BigDecimal total = new BigDecimal(0);
            for (Review review : reviews) {
                total = total.add(review.getRating());
            }
            double totalRating = total.doubleValue();
            double rating = totalRating / reviews.size();
            String format = new DecimalFormat("#.0").format(rating);
            productVo.setRating(Double.parseDouble(format));
        }
        cacheClient.setWithLogicalExpire(CACHE_SHOP_KEY + product.getProductId(), productVo, 10L, TimeUnit.SECONDS);
    }

    @Test
    void importStock() {
        List<Product> list = productService.list();
        log.info("List：" + list);
        for (Product product : list) {
            Long productId = product.getProductId();
            Integer stock = product.getStock();
            stringRedisTemplate.opsForValue().set(PRODUCT_STOCK_KEY + productId, String.valueOf(stock));
        }
    }

    @Test
    void testLua() {

        List<String> list = new ArrayList();
        list.add(PRODUCT_STOCK_KEY + "1");
        list.add(PRODUCT_STOCK_KEY + "2");
        list.add(PRODUCT_STOCK_KEY + "3");

        String[] args = new String[list.size() + 1];
        args[0] = String.valueOf(list.size());
        args[1] = "1";
        args[2] = "1";
        args[3] = "1";

        Long execute = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                list,
                args);
        System.out.println(execute);
    }

    @Test
    void testScan() {
    }
}
