package com.ze.pigSale.service;

import com.ze.pigSale.entity.OrderDetail;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-05-20:33
 */

public interface OrdersDetailService {
    /**
     * 批量保存
     * @param orderDetails
     */
    void saveBatch(List<OrderDetail> orderDetails);

    /**
     * 获取某订单详情
     * @param OrderId
     * @return
     */
    List<OrderDetail> getListByOrderId(Long OrderId);
}

