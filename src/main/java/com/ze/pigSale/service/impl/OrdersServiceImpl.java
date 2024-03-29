package com.ze.pigSale.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.enums.PermissionEnum;
import com.ze.pigSale.utils.CommonUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zeb
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
    private OrderDetailService ordersDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserPermissionService userPermissionService;

    @Override
    public Orders getById(Long ordersId) {
        return ordersMapper.getById(ordersId);
    }

    @Override
    public PageInfo<OrdersDto> getPageWithDetail(int currentPage, int pageSize, Long ordersId, LocalDateTime beginTime, LocalDateTime endTime) {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.VIEW_ORDER);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }

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
    public void submit(OrdersDto ordersDto) {
        //获取用户信息
        Long userId = BaseContext.getCurrentId();
        User user = userService.getUserById(userId);
        log.info("user:{}", user);

        //获取地址信息
        Long addressId = ordersDto.getAddressId();
        Address address = addressService.getAddressById(addressId);
        if (address == null) {
            throw new CustomException("地址不存在");
        }

        //获取购买商品
        List<Long> cartItemIds = ordersDto.getCartItemIds();
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new CustomException("请选择商品");
        }

        List<Cart> cartList = cartService.getCartListByIds(cartItemIds);
        if (cartList == null || cartList.isEmpty()) {
            throw new CustomException("商品不存在");
        }

        //设置订单id
        SnowFlake idWorker = new SnowFlake(0, 0);
        ordersDto.setId(idWorker.nextId());

        //计算总金额
        BigDecimal amount = new BigDecimal(0);
        for (Cart cart : cartList) {
            amount = amount.add(cart.getAmount().multiply(new BigDecimal(cart.getQuantity())));
        }

        //设置订单明细信息
        List<OrderDetail> orderDetails = cartList.stream().map(item -> {
            //获取商品，判断商品库存是否足够
            Long productId = item.getProductId();
            Product product = productService.getProductById(productId);
            Integer stock = product.getStock();
            if (stock < item.getQuantity()) {
                throw new CustomException("提交订单失败, 商品" + product.getProductName() + "库存不足");
            }

            //减少商品库存
            product.setStock(stock - item.getQuantity());
            productService.updateProduct(product);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(ordersDto.getId());
            orderDetail.setPrice(item.getAmount());
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setProductId(item.getProductId());
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
        cartService.deleteBatch(cartItemIds);
    }

    @Override
    public PageInfo<OrdersDto> getListByUserId(int currentPage, int pageSize, LocalDateTime beginTime, LocalDateTime endTime) {
        //开启分页
        PageMethod.startPage(currentPage, pageSize);

        //查询某用户订单
        Long userId = BaseContext.getCurrentId();
        List<Orders> orders = ordersMapper.getListByUserId(userId, beginTime, endTime);

        //获取pageInfo
        PageInfo<Orders> ordersPageInfo = new PageInfo<>(orders);
        PageInfo<OrdersDto> ordersDtoPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(ordersPageInfo, ordersDtoPageInfo);

        //设置订单详情
        List<OrdersDto> ordersDtos = orders.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);

            List<OrderDetail> orderDetails = ordersDetailService.getListByOrderId(item.getId());
            ordersDto.setOrderDetails(orderDetails);

            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPageInfo.setList(ordersDtos);
        return ordersDtoPageInfo;
    }

    @Override
    public void updateStatus(Orders orders) {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.EDIT_ORDER);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }

        //根据id获取此订单
        Orders oneOrders = ordersMapper.getById(orders.getId());
        if (oneOrders == null) {
            throw new CustomException("无法修改，订单id错误");
        }

        //修改状态
        Integer status = orders.getStatus();
        oneOrders.setStatus(status);

        //根据id修改订单
        ordersMapper.updateById(oneOrders);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void again(Orders orders) {
        //根据id获取此订单
        Orders oneOrders = ordersMapper.getById(orders.getId());

        //设置订单id
        SnowFlake idWorker = new SnowFlake(0, 0);
        oneOrders.setId(idWorker.nextId());
        //设置下单时间
        oneOrders.setCreateTime(LocalDateTime.now());
        oneOrders.setCheckoutTime(LocalDateTime.now());
        //设置订单状态
        oneOrders.setStatus(2);

        //保存此订单
        ordersMapper.save(oneOrders);

        //查看订单明细
        List<OrderDetail> orderDetails = ordersDetailService.getListByOrderId(orders.getId());

        //修改订单明细
        orderDetails = orderDetails.stream().map(item -> {
            item.setOrderId(oneOrders.getId());
            return item;
        }).collect(Collectors.toList());

        //保存订单明细
        ordersDetailService.saveBatch(orderDetails);
    }


    @Override
    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void agree(Orders orders) {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.CANCEL_ORDER);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }

        Orders oneOrders = this.getById(orders.getId());
        if (oneOrders == null) {
            throw new CustomException("订单id错误");
        }

        oneOrders.setStatus(6);
        this.updateById(oneOrders);

        //增加产品库存
        List<OrderDetail> list = ordersDetailService.getListByOrderId(orders.getId());
        list.stream().map(item->{
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();
            Product product = productService.getProductById(productId);
            product.setStock(product.getStock() + quantity);
            productService.updateProduct(product);
            return item;
        }).collect(Collectors.toList());
    }

    @Override
    public void disagree(Orders orders, HttpServletRequest request) {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.CANCEL_ORDER);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }

        //获取订单
        Orders oneOrders = this.getById(orders.getId());
        if (oneOrders == null) {
            throw new CustomException("订单id错误");
        }

        //获取原先订单状态
        Object userStatusObj = request.getSession().getAttribute("userStatus");

        //设置订单状态，默认值2
        int userStatus = 2;

        if (userStatusObj != null) {
            userStatus = (Integer) userStatusObj;
        }
        oneOrders.setStatus(userStatus);

        //修改订单状态
        this.updateById(oneOrders);
    }

    @Override
    public Integer getCountByTime(LocalDateTime start, LocalDateTime end) {
        return ordersMapper.getCountByTime(start, end);
    }

}
