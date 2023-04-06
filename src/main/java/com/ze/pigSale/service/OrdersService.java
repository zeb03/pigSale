package com.ze.pigSale.service;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.dto.OrdersDto;
import com.ze.pigSale.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-04-20:29
 */
public interface OrdersService {

    /**
     * 管理员获取订单信息n
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageInfo<OrdersDto> getPageWithDetail(Integer currentPage, Integer pageSize, Long ordersId, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 提交订单
     * @param ordersDto
     */
    void submit(OrdersDto ordersDto);
}
