package com.ze.pigSale.service.impl;

import com.ze.pigSale.entity.OrderDetail;
import com.ze.pigSale.mapper.OrderDetailMapper;
import com.ze.pigSale.service.OrdersDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-05-20:33
 */
@Service
public class OrdersDetailServiceImpl implements OrdersDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public void saveBatch(List<OrderDetail> orderDetails) {
        orderDetailMapper.saveBatch(orderDetails);
    }
}
