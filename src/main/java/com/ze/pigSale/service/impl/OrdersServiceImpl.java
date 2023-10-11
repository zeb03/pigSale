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

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.anno.PermissionAnno;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.constants.ExceptionConstants;
import com.ze.pigSale.dto.OrderMQ;
import com.ze.pigSale.enums.PermissionEnum;
import com.ze.pigSale.utils.SnowFlake;
import com.ze.pigSale.dto.OrdersDTO;
import com.ze.pigSale.entity.*;
import com.ze.pigSale.mapper.OrdersMapper;
import com.ze.pigSale.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ze.pigSale.constants.MqConstants.ORDER_QUEUE_NAME;
import static com.ze.pigSale.constants.OrderConstants.*;
import static com.ze.pigSale.constants.OrderConstants.ORDER_HAS_CANCEL;
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
    private RabbitTemplate rabbitTemplate;

    private static final DefaultRedisScript<Long> ORDER_SCRIPT;

    static {
        ORDER_SCRIPT = new DefaultRedisScript<>();
        ORDER_SCRIPT.setLocation(new ClassPathResource("order.lua"));
        ORDER_SCRIPT.setResultType(Long.class);
    }

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submit(OrdersDTO ordersDto) {
        // 获取购买的商品id
        List<Long> cartItemIds = ordersDto.getCartItemIds();
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new CustomException("请选择商品");
        }

        // 获取购物车商品
        List<Cart> cartList = cartService.getCartListByIds(cartItemIds);
        if (cartList == null || cartList.isEmpty()) {
            throw new CustomException("商品不存在");
        }
        // 判断库存是否充足，最好使用Lua脚本(如何使用for)，保证查询和扣减一致性
        // 创建keys，保存商品id
        List<String> keys = new ArrayList<>(cartList.size());
        // 创建args，保存数量
        String[] args = new String[cartList.size() + 1];
        // 设置keys长度
        args[0] = String.valueOf(cartList.size());

        for (int i = 0; i < cartList.size(); i++) {
            Cart cart = cartList.get(i);

            Long productId = cart.getProductId();
            Integer quantity = cart.getQuantity();

            keys.add(productId.toString());
            args[i + 1] = quantity.toString();
        }

        // 执行lua脚本
        Long result = stringRedisTemplate.execute(ORDER_SCRIPT, keys, args);

        if (result != 0) {
            throw new CustomException("商品id: " + result + "库存不足，请重新选择");
        }

        // for (Cart cart : cartList) {
        // String cacheStock = stringRedisTemplate.opsForValue().get(RedisConstants.PRODUCT_STOCK_KEY + cart.getProductId());
        // if (StringUtil.isBlank(cacheStock)) {
        // throw new CustomException("未缓存改商品库存数据");
        // }
        // int stock = Integer.parseInt(cacheStock);
        // //库存不足
        // if (stock <= cart.getQuantity()) {
        // throw new CustomException("购物车中商品" + cart.getProductId() + "库存不足，请重新选择");
        // }
        // }
        // //减少redis库存
        // for (Cart cart : cartList) {
        // String cacheStock = stringRedisTemplate.opsForValue().get(RedisConstants.PRODUCT_STOCK_KEY + cart.getProductId());
        // assert cacheStock != null;
        // int stock = Integer.parseInt(cacheStock);
        // stringRedisTemplate.opsForValue().set(PRODUCT_STOCK_KEY + cart.getProductId(), String.valueOf(stock - cart.getQuantity()));
        // }

        // 设置订单id
        SnowFlake idWorker = new SnowFlake(0, 0);
        ordersDto.setId(idWorker.nextId());

        // 创建消息对象
        OrderMQ orderMq = new OrderMQ();
        orderMq.setOrderId(ordersDto.getId());
        orderMq.setCartId(cartItemIds);
        orderMq.setAddress(ordersDto.getAddressId());
        orderMq.setUserId(BaseContext.getCurrentId());

        // 发送订单号、商品列表、用户号到消息队列
        rabbitTemplate.convertAndSend(ORDER_QUEUE_NAME, JSONUtil.toJsonStr(orderMq));

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
    @PermissionAnno(value = PermissionEnum.EDIT_ORDER)
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
    @PermissionAnno(value = CANCEL_ORDER)
    public void agree(Orders orders) {

        Orders oneOrders = this.getById(orders.getId());
        if (oneOrders == null) {
            throw new CustomException("订单id错误");
        }

        oneOrders.setStatus(6);
        this.updateById(oneOrders);

        // 增加产品库存
        List<OrderDetail> list = ordersDetailService.getListByOrderId(orders.getId());
        list.stream().map(item -> {
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();
            Product product = productService.getProductById(productId);
            product.setStock(product.getStock() + quantity);
            productService.updateProduct(product);
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
