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
import com.ze.pigSale.dto.OrderMQ;
import com.ze.pigSale.enums.PermissionEnum;
import com.ze.pigSale.mq.producer.DelayCloseOrderSendProduce;
import com.ze.pigSale.service.handler.OrderCreateChainContext;
import com.ze.pigSale.utils.SnowFlake;
import com.ze.pigSale.dto.OrdersDTO;
import com.ze.pigSale.entity.*;
import com.ze.pigSale.mapper.OrdersMapper;
import com.ze.pigSale.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ze.pigSale.constants.MQConstants.ORDER_ASYNC_SUBMIT_TOPIC_KEY;
import static com.ze.pigSale.constants.OrderStatusConstants.*;
import static com.ze.pigSale.constants.OrderStatusConstants.ORDER_HAS_CANCEL;
import static com.ze.pigSale.constants.RedisConstants.*;
import static com.ze.pigSale.enums.PermissionEnum.CANCEL_ORDER;

/**
 * @author: zeb
 * Date: 2023-04-04-20:32
 */
@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private CartService cartService;
    @Resource
    private OrderDetailService ordersDetailService;
    @Resource
    private ProductService productService;
    @Resource
    private UserPermissionService userPermissionService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private OrdersService ordersService;
    @Resource
    private OrderCreateChainContext<OrdersDTO> orderCreateChainContext;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private DelayCloseOrderSendProduce delayCloseOrderSendProduce;

    @Override
    public Orders getById(Long ordersId) {
        return ordersMapper.getById(ordersId);
    }

    @Override
    @PermissionAnno(value = PermissionEnum.VIEW_ORDER)
    public PageInfo<OrdersDTO> getPageWithDetail(int currentPage, int pageSize, Long ordersId, LocalDateTime beginTime, LocalDateTime endTime) {

        // 开启分页
        PageMethod.startPage(currentPage, pageSize);

        // 查询在指定范围时间内符合条件的所有订单
        List<Orders> orders = ordersMapper.listByTime(ordersId, beginTime, endTime);

        PageInfo<Orders> ordersPageInfo = new PageInfo<>(orders);
        PageInfo<OrdersDTO> ordersDtoPageInfo = new PageInfo<>();

        BeanUtils.copyProperties(ordersPageInfo, ordersDtoPageInfo);

        log.info("order/page:{}", ordersPageInfo);
        // 查询订单下所有商品
        List<OrdersDTO> ordersDtos = orders.stream().map(item -> {
            OrdersDTO ordersDto = new OrdersDTO();
            BeanUtils.copyProperties(item, ordersDto);

            List<OrderDetail> orderDetails = ordersDetailService.getListByOrderId(item.getId());
            ordersDto.setOrderDetails(orderDetails);

            String name = "用户" + item.getUserId();
            ordersDto.setUserName(name);
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPageInfo.setList(ordersDtos);

        return ordersDtoPageInfo;
    }

    /**
     * 该方法为购物车下单，但包含了商品超卖的情况，实际业务可以将商品秒杀单独处理
     *
     * @param ordersDto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submit(OrdersDTO ordersDto) {
        // 责任链验证
        orderCreateChainContext.handle(ordersDto);

        // 设置订单id
        SnowFlake idWorker = new SnowFlake(0, 0);
        ordersDto.setId(idWorker.nextId());

        // 创建消息对象
        OrderMQ orderMq = new OrderMQ();
        orderMq.setOrderId(ordersDto.getId());
        orderMq.setCartId(ordersDto.getCartItemIds());
        orderMq.setAddress(ordersDto.getAddressId());
        orderMq.setUserId(BaseContext.getCurrentId());

        // 发送订单号、商品列表、用户号到消息队列，异步处理解耦，缩短业务链
        // rabbitTemplate.convertAndSend(ORDER_QUEUE_NAME, JSONUtil.toJsonStr(orderMq));
        rocketMQTemplate.asyncSend(ORDER_ASYNC_SUBMIT_TOPIC_KEY, MessageBuilder.withPayload(orderMq).build(), new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {
                // 处理消息发送成功逻辑
                log.info("订单消息发送成功");
                //实现订单延迟10分钟关闭
                delayCloseOrderSendProduce.sendDelayMsg(ordersDto, 14);
            }

            @Override
            public void onException(Throwable throwable) {
                // 处理消息发送异常逻辑
                log.info("订单消息发送失败");
                log.info("{}", throwable);
                throw new CustomException("订单消息发送失败");
            }
        });

        // 返回订单号给用户
    }

    @Override
    public PageInfo<OrdersDTO> getListByUserId(int currentPage, int pageSize, LocalDateTime beginTime, LocalDateTime endTime) {
        // 开启分页
        PageMethod.startPage(currentPage, pageSize);

        // 查询某用户订单
        Long userId = BaseContext.getCurrentId();
        List<Orders> orders = ordersMapper.getListByUserId(userId, beginTime, endTime);

        // 获取pageInfo
        PageInfo<Orders> ordersPageInfo = new PageInfo<>(orders);
        PageInfo<OrdersDTO> ordersDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(ordersPageInfo, ordersDtoPageInfo);

        // 设置订单详情
        List<OrdersDTO> ordersDtos = orders.stream().map(item -> {
            OrdersDTO ordersDto = new OrdersDTO();
            BeanUtils.copyProperties(item, ordersDto);

            List<OrderDetail> orderDetails = ordersDetailService.getListByOrderId(item.getId());
            ordersDto.setOrderDetails(orderDetails);

            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPageInfo.setList(ordersDtos);
        return ordersDtoPageInfo;
    }

    @Override
//    @PermissionAnno(value = PermissionEnum.EDIT_ORDER)
    public void updateStatus(Orders orders) {
        // 根据id获取此订单
        Orders oneOrders = ordersMapper.getById(orders.getId());
        if (oneOrders == null) {
            throw new CustomException("无法修改，订单id错误");
        }

        // 修改状态
        Integer status = orders.getStatus();
        oneOrders.setStatus(status);

        // 根据id修改订单
        ordersMapper.updateById(oneOrders);

        log.info("用户修改订单状态为{}", orders.getStatus());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void again(Orders orders) {
        // 根据id获取此订单
        Orders oneOrders = ordersMapper.getById(orders.getId());

        // 设置订单id
        SnowFlake idWorker = new SnowFlake(0, 0);
        oneOrders.setId(idWorker.nextId());
        // 设置下单时间
        oneOrders.setCreateTime(LocalDateTime.now());
        oneOrders.setCheckoutTime(LocalDateTime.now());
        // 设置订单状态
        oneOrders.setStatus(2);

        // 保存此订单
        ordersMapper.save(oneOrders);

        // 查看订单明细
        List<OrderDetail> orderDetails = ordersDetailService.getListByOrderId(orders.getId());

        // 修改订单明细
        orderDetails = orderDetails.stream().map(item -> {
            item.setOrderId(oneOrders.getId());
            return item;
        }).collect(Collectors.toList());

        // 保存订单明细
        ordersDetailService.saveBatch(orderDetails);
    }

    @Override
    public void updateOrdersById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
//    @PermissionAnno(value = CANCEL_ORDER)
    public void agree(Orders orders) {

        Orders oneOrders = this.getById(orders.getId());
        if (BeanUtil.isEmpty(oneOrders)) {
            throw new CustomException("订单id错误");
        }

        oneOrders.setStatus(ORDER_HAS_CANCEL);
        this.updateById(oneOrders);

        // 增加产品库存
        List<OrderDetail> list = ordersDetailService.getListByOrderId(orders.getId());
        list.stream().map(item -> {
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();
            Product product = productService.getProductById(productId);
            product.setStock(product.getStock() + quantity);
            productService.updateProduct(product);
            //缓存更新，使用canal订阅数据库
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    @PermissionAnno(value = CANCEL_ORDER)
    public void disagree(Orders orders, HttpServletRequest request) {

        // 获取订单
        Orders oneOrders = this.getById(orders.getId());
        if (oneOrders == null) {
            throw new CustomException("订单id错误");
        }

        // 获取原先订单状态
        Object userStatusObj = request.getSession().getAttribute("userStatus");

        // 设置订单状态，默认值2
        int userStatus = 2;

        if (userStatusObj != null) {
            userStatus = (Integer) userStatusObj;
        }
        oneOrders.setStatus(userStatus);

        // 修改订单状态
        this.updateById(oneOrders);
    }

    @Override
    public Integer getCountByTime(LocalDateTime start, LocalDateTime end) {
        return ordersMapper.getCountByTime(start, end);
    }

    /**
     * 申请退款
     *
     * @param ordersId
     * @param request
     * @return
     */
    @Override
    public Result<String> cancelOrders(Long ordersId, HttpServletRequest request) {
        // 获取此订单
        Orders oneOrders = ordersService.getById(ordersId);

        // 判断订单状态
        if (oneOrders == null) {
            throw new CustomException("订单id错误");
        }

        Integer status = oneOrders.getStatus();
        if (status.equals(ORDER_HAS_CANCEL)) {
            throw new CustomException("该订单已取消");
        }

        if (status.equals(ORDER_NOT_PAY)) {
            // 设置订单状态
            oneOrders.setStatus(ORDER_HAS_CANCEL);
        } else {
            // TODO:此处可以缓存
            request.getSession().setAttribute("userStatus", oneOrders.getStatus());
            oneOrders.setStatus(ORDER_APPLY_CANCEL);
        }

        ordersService.updateById(oneOrders);

        if (oneOrders.getStatus().equals(ORDER_HAS_CANCEL)) {
            return Result.success("取消成功");
        }

        // 将消息推送至相关管理员收件箱
        sendMsgToAdmin(ordersId);

        // TODO:超时未处理则复原订单状态：https://blog.csdn.net/Anenan/article/details/126368753

        return Result.success("等待管理员同意");
    }

    @Override
    public boolean closeOrders(OrdersDTO ordersDTO) {
        Long ordersId = ordersDTO.getId();
        log.info("[关闭订单]ordersId:{}", ordersId);
        //这里要查看最新状态
        Orders orders = ordersService.getById(ordersId);
        if (BeanUtil.isEmpty(orders)) {
            throw new CustomException("订单无法获取");
        }
        //如果状态是未支付，则关闭订单，否则返回false
        if (!Objects.equals(orders.getStatus(), ORDER_NOT_PAY)) {
            return false;
        }

        //设置订单状态
        //恢复商品库存
        agree(orders);
        log.info("关闭订单成功");
        return true;
    }

    @Override
    public void pay(Orders orders) {
        // TODO: 测试使用
        orders.setStatus(2);
        ordersService.updateStatus(orders);
    }

    /**
     * 推送消息
     *
     * @param ordersId
     */
    private void sendMsgToAdmin(Long ordersId) {
        // 查询相关管理员
        // 遍历数据库表，找到有订单管理权限的管理员
        Integer permissionId = CANCEL_ORDER.getPermissionId();
        LambdaQueryWrapper<UserPermissions> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPermissions::getPermissionId, permissionId);
        List<UserPermissions> list = userPermissionService.list(wrapper);
        for (UserPermissions item : list) {
            // 以用户id为key，订单号为value，时间戳为score保存到redis
            Long userId = item.getUserId();
            String key = FEED_ORDER_KEY + userId;
            stringRedisTemplate.opsForZSet().add(key, ordersId.toString(), System.currentTimeMillis());
        }
    }

}
