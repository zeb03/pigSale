package com.ze.pigSale.service.impl;

import com.ze.pigSale.entity.OrderDetail;
import com.ze.pigSale.mapper.OrderDetailMapper;
import com.ze.pigSale.service.OrderDetailService;
import com.ze.pigSale.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-05-20:33
 */
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public void saveBatch(List<OrderDetail> orderDetails) {
        orderDetailMapper.saveBatch(orderDetails);
    }

    @Override
    public List<OrderDetail> getListByOrderId(Long orderId) {
        return orderDetailMapper.getListByOrderId(orderId);
    }

    @Override
    public List<OrderDetailVo> getByNoReview(Long userId) {
        return orderDetailMapper.getByNoReview(userId);
    }
}
