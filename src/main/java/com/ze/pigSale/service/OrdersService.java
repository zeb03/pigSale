package com.ze.pigSale.service;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.dto.OrdersDto;
import com.ze.pigSale.entity.Orders;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * author: zebii
 * Date: 2023-04-04-20:29
 */
public interface OrdersService {

    /**
     * 根据id获取订单
     *
     * @param orders
     * @return
     */
    Orders getById(Long ordersId);

    /**
     * 管理员获取订单信息
     * 可以模糊查询订单号，按指定时间范围进行修改
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageInfo<OrdersDto> getPageWithDetail(int currentPage, int pageSize, Long ordersId, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 提交订单
     *
     * @param ordersDto
     */
    void submit(OrdersDto ordersDto);

    /**
     * 查看用户的所有订单
     *
     * @return
     */
    PageInfo<OrdersDto> getListByUserId(int currentPage, int pageSize, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 修改订单
     *
     * @param orders
     */
    void updateStatus(Orders orders);

    /**
     * 再来一单
     *
     * @param orders
     */
    void again(Orders orders);

    /**
     * 修改订单
     *
     * @param orders
     */
    void updateById(Orders orders);

    /**
     * 同意退款
     *
     * @param orders
     */
    void agree(Orders orders);

    /**
     * 拒接退款
     *
     * @param orders
     */
    void disagree(Orders orders, HttpServletRequest request);
}
