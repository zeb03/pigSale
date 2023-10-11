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

package com.ze.pigSale.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.anno.PermissionAnno;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.constants.ExceptionConstants;
import com.ze.pigSale.dto.OrderDetailDTO;
import com.ze.pigSale.entity.*;
import com.ze.pigSale.enums.PermissionEnum;
import com.ze.pigSale.mapper.OrderDetailMapper;
import com.ze.pigSale.mapper.ProductMapper;
import com.ze.pigSale.service.*;
import com.ze.pigSale.utils.CacheClient;
import com.ze.pigSale.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ze.pigSale.constants.MqConstants.*;
import static com.ze.pigSale.constants.RedisConstants.*;
import static com.ze.pigSale.enums.PermissionEnum.*;

/**
 * author: zebii
 * Date: 2023-03-15-20:11
 */
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CartService cartService;

    @Resource
    private OrdersService ordersService;

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ReviewService reviewService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private UserProductService userProductService;

    @Override
    public Product getProductById(Long id) {
        return productMapper.getProductById(id);
    }

    @Override
    public List<Product> getProductList(String productName, Long categoryId) {
        return productMapper.getAllProduct(productName, categoryId);
    }

    @Override
    public List<Product> getAllProductByName(String name) {
        return productMapper.getAllProductByName(name);
    }

    @Override
    @PermissionAnno(value = ADD_PRODUCT)
    public void insertProduct(Product product) {

        // 设置时间
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());

        // 添加
        productMapper.insertProduct(product);

        // 将消息发布到队列，同步到es
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, INSERT_KEY, product.getProductId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @PermissionAnno(value = EDIT_PRODUCT)
    public void updateProduct(Product product) {

        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateProduct(product);

        // 删除redis缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + product.getProductId());

        // 消息推送
        this.sendMessage(product.getProductId());

        // 若购物车存在该商品，则也要进行修改
        Cart cart = cartService.getCart(product.getProductId());
        if (cart != null) {
            String productName = product.getProductName();
            BigDecimal price = product.getPrice();
            String image = product.getImage();
            cart.setAmount(price);
            cart.setName(productName);
            cart.setImage(image);
            cartService.updateCartById(cart);
        }
        // 将消息发布到队列，同步到es
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, INSERT_KEY, product.getProductId());
    }

    private void sendMessage(Long productId) {
        log.info("productId" + productId);
        // 查看所有收藏商品的用户
        LambdaQueryWrapper<UserProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProduct::getProductId, productId);
        List<UserProduct> list = userProductService.list(wrapper);
        log.info("list" + list);
        // 获取用户id
        List<Long> userList = list.stream().map(UserProduct::getUserId).collect(Collectors.toList());

        // 将消息发送到对应用户的收件箱，以用户标识为key，以产品id为value，以时间戳为score
        for (Long userId : userList) {
            String key = FEED_SHOP_KEY + userId;
            stringRedisTemplate.opsForZSet().add(key, productId.toString(), System.currentTimeMillis());
        }
    }

    @Override
    @PermissionAnno(value = DELETE_PRODUCT)
    public void deleteProduct(Long productId) {
        productMapper.deleteProduct(productId);
        // 删除redis缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + productId);
        // 将消息发布到队列，同步到es
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, DELETE_KEY, productId);
    }

    @Override
    @PermissionAnno(value = VIEW_DATA)
    public Map<String, Integer> getSalesRank(Integer month) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now.minusMonths(month);

        log.info("now:" + now);
        log.info("time:" + time);

        List<OrderDetailDTO> list = orderDetailMapper.getListByTime(now, time);

        HashMap<String, Integer> rankMap = new LinkedHashMap<>();

        list.stream().map(item -> {

            Long productId = item.getProductId();

            Integer quantity = item.getQuantity();

            Product product = productMapper.getProductById(productId);

            if (product == null) {
                return null;
            }

            String productName = product.getProductName();

            rankMap.put(productName, rankMap.getOrDefault(productName, 0) + quantity);

            return null;
        }).collect(Collectors.toList());

        return rankMap;
    }

    @Override
    @PermissionAnno(value = VIEW_DATA)
    public Map<String, BigDecimal> getAllBenefit(Integer month) {

        if (month == null) {
            throw new CustomException("参数为空");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now.minusMonths(month);

        log.info("now:" + now);
        log.info("time:" + time);

        List<OrderDetailDTO> list = orderDetailMapper.getListByTime(now, time);

        HashMap<String, BigDecimal> benefitMap = new LinkedHashMap<>();

        list.stream().map(item -> {

            Long productId = item.getProductId();

            Integer quantity = item.getQuantity();

            Product product = productMapper.getProductById(productId);

            String productName = product.getProductName();

            benefitMap.put(productName, benefitMap.getOrDefault(productName, BigDecimal.ZERO)
                    .add(product.getPrice().multiply(new BigDecimal(quantity))));

            return null;
        }).collect(Collectors.toList());

        return benefitMap;
    }

    @Override
    @PermissionAnno(value = VIEW_DATA)
    public List<BigDecimal> getBenefit() {

        // 获取当前年份
        int currentYear = YearMonth.now().getYear();

        // 构建收益数据列表
        List<BigDecimal> benefitData = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            // 构建当前年份和月份对应的YearMonth对象
            YearMonth yearMonth = YearMonth.of(currentYear, month);

            // 根据年份和月份获取起始时间和结束时间
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            // 根据YearMonth查询对应的订单详情列表
            List<OrderDetailDTO> orders = orderDetailMapper.getListByTime(endDateTime, startDateTime);

            // 计算月份收益
            BigDecimal monthlyTotal = calculateMonthlyBenefit(orders);
            benefitData.add(monthlyTotal);
        }

        return benefitData;
    }

    @Override
    @PermissionAnno(value = VIEW_DATA)
    public List<Integer> getOrderCount() {

        // 获取当前年份
        int currentYear = YearMonth.now().getYear();

        // 构建收益数据列表
        List<Integer> orderCount = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            // 构建当前年份和月份对应的YearMonth对象
            YearMonth yearMonth = YearMonth.of(currentYear, month);

            // 根据年份和月份获取起始时间和结束时间
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            // 根据YearMonth查询对应的订单详情列表
            Integer count = ordersService.getCountByTime(startDateTime, endDateTime);
            orderCount.add(count);
        }

        return orderCount;
    }

    @Override
    public Result detail(Long productId) {
        ProductVo productVo = cacheClient.queryVoWithLogicalExpire(CACHE_SHOP_KEY, productId, Product.class, productMapper::getProductById,
                ProductVo.class, this::setProductVo, CACHE_SHOP_TTL, TimeUnit.MINUTES);
        return Result.success(productVo);
        // 解决缓存穿透
        // //从redis获取json字符串
        // String jsonStr = stringRedisTemplate.opsForValue().get(CACHE_SHOP_KEY + productId);
        // //如果存在则返回
        // if (StringUtil.isNotBlank(jsonStr)) {
        // Product product = JSONUtil.toBean(jsonStr, Product.class);
        // return Result.success(product);
        // }
        // //如果不存在，则查看是否为空
        // if (jsonStr != null) {
        // return Result.success(null);
        // }
        // //从数据库查找
        // Product product = productMapper.getProductById(productId);
        // if (product == null) {
        // stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY + productId, "", CACHE_SHOP_TTL, TimeUnit.MINUTES);
        // return Result.success(null);
        // }
        // ProductVo productVo = BeanUtil.copyProperties(product, ProductVo.class);
        // Category category = categoryService.getCategoryById(productVo.getCategoryId());
        // if (category != null) {
        // productVo.setCategoryName(category.getCategoryName());
        // }
        //
        // //获取商品评分
        // List<Review> reviews = reviewService.getListByProduct(productId);
        // if (reviews == null || reviews.isEmpty()) {
        // productVo.setRating(0.0);
        // } else {
        // BigDecimal total = new BigDecimal(0);
        // for (Review review : reviews) {
        // total = total.add(review.getRating());
        // }
        // double totalRating = total.doubleValue();
        // double rating = totalRating / reviews.size();
        // String format = new DecimalFormat("#.0").format(rating);
        // productVo.setRating(Double.parseDouble(format));
        // }
        //
        // //保存到redis
        // stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY + productId, JSONUtil.toJsonStr(productVo), CACHE_SHOP_TTL, TimeUnit.MINUTES);
    }

    @Override
    public Result collectProduct(Long productId) {
        Long userId = BaseContext.getCurrentId();
        log.info("user:" + userId);
        String key = COLLECT_USER_KEY + userId;
        // 查询zset集合
        Double score = stringRedisTemplate.opsForZSet().score(key, productId.toString());
        if (score == null) {
            // 加入收藏集合
            UserProduct userProduct = new UserProduct();
            userProduct.setUserId(userId);
            userProduct.setProductId(productId);
            userProductService.saveOrUpdate(userProduct);
            // 以用户标识为key，以商品id为value，以时间戳为score存入zset
            stringRedisTemplate.opsForZSet().add(key, String.valueOf(productId), System.currentTimeMillis());
            return Result.success("收藏成功");
        }
        // 已收藏过，取消收藏
        LambdaQueryWrapper<UserProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(UserProduct::getUserId, userId).ge(UserProduct::getProductId, productId);
        userProductService.remove(wrapper);
        stringRedisTemplate.opsForZSet().remove(key, productId.toString());
        return Result.success("取消收藏成功");
    }

    @Override
    public Result getCollections() {
        // 获取用户id
        Long userId = BaseContext.getCurrentId();
        String key = COLLECT_USER_KEY + userId;
        // 查询用户zset收藏集合
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, System.currentTimeMillis());
        if (typedTuples == null) {
            return Result.success(Collections.emptyList());
        }
        // 根据id查询商品
        List<Product> list =
                typedTuples.stream().map(item -> {
                    if (item.getValue() == null) {
                        return null;
                    }
                    Long productId = Long.valueOf(item.getValue());
                    Product product = this.getProductById(productId);
                    if (product == null) {
                        // 若商品下架，则从缓存移出
                        stringRedisTemplate.opsForZSet().remove(key, productId);
                        throw new CustomException("商品已下架");
                    }
                    return product;
                }).collect(Collectors.toList());
        log.info("List:" + list);
        // 根据ids查询数据库
        return Result.success(list);
    }

    @Override
    public Result<PageInfo<ProductVo>> getPage(Integer currentPage, Integer pageSize, String keyword, Long categoryId) {
        PageMethod.startPage(currentPage, pageSize);
        List<Product> productList = this.getProductList(keyword, categoryId);

        PageInfo<Product> sourcePageInfo = new PageInfo<>(productList);
        PageInfo<ProductVo> targetPageInfo = new PageInfo<>();

        BeanUtils.copyProperties(sourcePageInfo, targetPageInfo);

        List<ProductVo> productVoList = productList.stream().map((item -> {
            // 创建dto
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(item, productVo);

            // 获取种类名
            Long id = productVo.getCategoryId();
            Category category = categoryService.getCategoryById(id);

            if (category != null) {
                String name = category.getCategoryName();
                productVo.setCategoryName(name);
            }

            return productVo;
        })).collect(Collectors.toList());

        targetPageInfo.setList(productVoList);
        return Result.success(targetPageInfo);
    }

    private ProductVo setProductVo(Product product) {
        log.info("setProductVo" + product);
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
        return productVo;
    }

    /**
     * 根据订单详情计算月收益
     *
     * @param orderDetails
     * @return
     */
    private BigDecimal calculateMonthlyBenefit(List<OrderDetailDTO> orderDetails) {
        BigDecimal totalBenefit = BigDecimal.ZERO;

        for (OrderDetailDTO orderDetail : orderDetails) {
            BigDecimal price = orderDetail.getPrice();
            int quantity = orderDetail.getQuantity();

            BigDecimal benefit = price.multiply(BigDecimal.valueOf(quantity));
            totalBenefit = totalBenefit.add(benefit);
        }

        return totalBenefit;
    }

}
