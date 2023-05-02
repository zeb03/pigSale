package com.ze.pigSale.service;

import com.ze.pigSale.entity.OrderDetail;
import com.ze.pigSale.vo.OrderDetailVo;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-05-20:33
 */

public interface OrderDetailService {
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

    /**
     * 获取已经送达但是没有评价的订单
     * @param userId
     * @return
     */
    List<OrderDetailVo> getByNoReview(Long userId);
}

