package com.ze.pigSale.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.utils.SnowFlake;
import com.ze.pigSale.dto.OrdersDto;
import com.ze.pigSale.entity.*;
import com.ze.pigSale.mapper.OrdersMapper;
import com.ze.pigSale.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * author: zebii
 * Date: 2023-04-04-20:32
 */
@Service
@Slf4j
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrdersDetailService ordersDetailService;

    @Override
    public PageInfo<OrdersDto> getPageWithDetail(Integer currentPage, Integer pageSize, Long ordersId, LocalDateTime beginTime, LocalDateTime endTime) {
        //开启分页
        PageMethod.startPage(currentPage, pageSize);

        //查询在指定范围时间内符合条件的所有订单
        List<Orders> orders = ordersMapper.listByTime(ordersId, beginTime, endTime);

        PageInfo<Orders> ordersPageInfo = new PageInfo<>(orders);
        PageInfo<OrdersDto> ordersDtoPageInfo = new PageInfo<>();

        BeanUtils.copyProperties(ordersPageInfo, ordersDtoPageInfo);

        log.info("order/page:{}", ordersPageInfo);
        //查询订单下所有商品
        List<OrdersDto> ordersDtos = orders.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            String name = "用户" + item.getUserId();
            ordersDto.setUserName(name);
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPageInfo.setList(ordersDtos);

        return ordersDtoPageInfo;
    }

    @Transactional
    @Override
    public void submit(OrdersDto ordersDto) {
        //获取用户信息
        Long userId = BaseContext.getCurrentId();
        User user = userService.getUserById(userId);
        log.info("user:{}", user);

        //获取地址信息
        Long addressId = ordersDto.getAddressId();
        Address address = addressService.getAddressById(addressId);
        if (address == null) {
            throw new CustomException("用户地址信息有误，不能下单");
        }

        //获取商品信息
//        List<Cart> cartList = cartService.getCartList(userId);
//        if (cartList == null || cartList.isEmpty()) {
//            throw new CustomException("购物车为空，不能下单");
//        }
        if (ordersDto.getCartItemIds() == null || ordersDto.getCartItemIds().isEmpty()) {
            throw new CustomException("请选择商品");
        }
        List<Long> cartItemIds = ordersDto.getCartItemIds();
        List<Cart> cartList = cartService.getCartListByIds(cartItemIds);
        if (cartList == null || cartList.isEmpty()){
            throw new CustomException("无法下单，商品id错误");
        }

        //设置订单明细信息和计算总金额
//        AtomicInteger amount = new AtomicInteger(0);
        BigDecimal amount = new BigDecimal(0);
        SnowFlake idWorker = new SnowFlake(0, 0);
        ordersDto.setId(idWorker.nextId());

        for (Cart cart : cartList) {
            amount = amount.add(cart.getAmount().multiply(new BigDecimal(cart.getQuantity())));
        }

        List<OrderDetail> orderDetails = cartList.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(ordersDto.getId());
            orderDetail.setPrice(item.getAmount());
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setProductId(item.getProductId());
//            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getQuantity())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //保存订单明细
        log.info("orderDetails:{}", orderDetails);
        ordersDetailService.saveBatch(orderDetails);


        //设置订单信息
        ordersDto.setCreateTime(LocalDateTime.now());
        ordersDto.setCheckoutTime(LocalDateTime.now());
        ordersDto.setPayMethod(1);
        ordersDto.setStatus(2);
        ordersDto.setTotalPrice(new BigDecimal(String.valueOf(amount)));//总金额
        ordersDto.setUserId(userId);
        ordersDto.setUserName(user.getUsername());
        ordersDto.setPhone(user.getPhone());
        ordersDto.setAddress((address.getProvince() == null ? "" : address.getProvince())
                + (address.getCity() == null ? "" : address.getCity())
                + (address.getDistrict() == null ? "" : address.getDistrict())
                + (address.getDetail() == null ? "" : address.getDetail()));

        ordersMapper.save(ordersDto);

        //清空购物车
//        cartService.cleanCart(userId);
    }


}
