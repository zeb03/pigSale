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

package com.ze.pigSale.mq.comsumer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ze.pigSale.anno.Idempotent;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.constants.MQConstants;
import com.ze.pigSale.dto.OrderMQ;
import com.ze.pigSale.entity.*;
import com.ze.pigSale.enums.IdempotentSceneEnum;
import com.ze.pigSale.enums.IdempotentTypeEnum;
import com.ze.pigSale.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.ze.pigSale.constants.MQConstants.ORDER_ASYNC_SUBMIT_TOPIC_KEY;

/**
 * @author zeb
 * @Date 2024-01-24 16:24
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(topic = ORDER_ASYNC_SUBMIT_TOPIC_KEY, consumerGroup = MQConstants.ORDER_CON_GROUP)
public class OrderSubmitCustomer implements RocketMQListener<OrderMQ> {

    @Resource
    private ProductService productService;
    @Resource
    private CartService cartService;
    @Resource
    private OrderDetailService ordersDetailService;
    @Resource
    private UserService userService;
    @Resource
    private AddressService addressService;
    @Resource
    private OrdersService ordersService;

    @Override
    @Idempotent(
            uniqueKeyPrefix = "pigsale-order:pay_result_callback:",
            key = "#message.getUserId()+'_'+#message.hashCode()",
            type = IdempotentTypeEnum.SPEL,
            scene = IdempotentSceneEnum.MQ,
            keyTimeout = 7200L
    )
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(OrderMQ message) {
        log.info("监听到消息：message={}", message);
        Long orderId = message.getOrderId();
        Long userId = message.getUserId();
        Long addressId = message.getAddress();

        // 获取购物车项
        List<Long> list = message.getCartId();
        List<Cart> cartList = cartService.getCartListByIds(list);

        // 减少库存，设置订单明细
        List<OrderDetail> orderDetails = cartList.stream().map(item -> {
            // 获取商品
            Long productId = item.getProductId();
            Product product = productService.getProductById(productId);

            if (product == null) {
                throw new CustomException("商品为空");
            }

            // 获取购买数量
            Integer quantity = item.getQuantity();
            Integer stock = product.getStock();
            // 设置库存
            product.setStock(stock - quantity);
            // 使用乐观锁减少库存
            LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Product::getProductId, item.getProductId()).gt(Product::getStock, quantity);
            boolean successFlag = productService.saveOrUpdate(product, wrapper);

            // 库存超卖
            if (!successFlag) {
                throw new CustomException("商品库存不足");
            }

            // 设置订单明细信息
            return getOrderDetail(orderId, item);
        }).collect(Collectors.toList());

        log.info("orderDetail: " + orderDetails);
        log.info("cartList: " + cartList);

        // 保存订单明细
        ordersDetailService.saveBatch(orderDetails);

        // 获取订单对象
        // 计算总金额
        BigDecimal amount = new BigDecimal(0);
        for (Cart cart : cartList) {
            amount = amount.add(cart.getAmount().multiply(new BigDecimal(cart.getQuantity())));
        }
        Orders orders = setOrdersInfo(orderId, userId, addressId, amount);

        // 保存订单
        log.info("[保存订单]:{}", orders);
        ordersService.save(orders);

        // 清空购物车
        cartService.deleteBatch(list);
    }

    private OrderDetail getOrderDetail(Long orderId, Cart item) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setPrice(item.getAmount());
        orderDetail.setQuantity(item.getQuantity());
        orderDetail.setProductId(item.getProductId());
        return orderDetail;
    }

    private Orders setOrdersInfo(Long orderId, Long userId, Long addressId, BigDecimal amount) {
        // 获取用户信息
        User user = userService.getUserById(userId);
        // 获取地址信息
        Address address = addressService.getAddressById(addressId);
        // 创建订单对象
        Orders order = new Orders();
        order.setId(orderId);
        // 设置时间
        order.setCreateTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        // 设置支付方式
        order.setPayMethod(1);
        // 设置订单状态
        order.setStatus(1);
        // 设置总金额
        order.setTotalPrice(new BigDecimal(String.valueOf(amount)));
        // 设置用户id
        order.setUserId(userId);
        // 设置用户名
        order.setUserName(user.getUsername());
        // 设置手机号码
        order.setPhone(user.getPhone());
        // 设置地址
        order.setAddressId(addressId);
        order.setAddress(
                (address.getProvince() == null ? "" : address.getProvince())
                        + (address.getCity() == null ? "" : address.getCity())
                        + (address.getDistrict() == null ? "" : address.getDistrict())
                        + (address.getDetail() == null ? "" : address.getDetail()));
        return order;
    }
}
