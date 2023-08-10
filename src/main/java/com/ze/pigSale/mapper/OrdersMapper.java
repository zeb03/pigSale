package com.ze.pigSale.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ze.pigSale.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author z
 * @description 针对表【orders】的数据库操作Mapper
 * @createDate 2023-04-04 20:21:04
 * @Entity com.ze.pigSale.entity.Orders
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 查询范围时间内的所有订单号
     *
     * @return
     */
    List<Orders> listByTime(@Param("ordersId") Long ordersId, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    List<Orders> list();

    /**
     * 根据id获取订单
     * @param id
     * @return
     */
    Orders getById(@Param("id") Long id);

    /**
     * 提交订单
     *
     * @param orders
     */
    void save(Orders orders);

    /**
     * 查看用户的所有订单
     *
     * @return
     */
    List<Orders> getListByUserId(@Param("userId") Long userId, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 修改订单
     * @param orders
     */
    void updateOrdersById(Orders orders);

    /**
     * 获取已经送达但是没有评价的订单
     * @param userId
     * @return
     */
    List<Orders> getByNoReview(@Param("userId") Long userId);

    /**
     * 获取指定时间内的订单数量
     * @param start
     * @param end
     * @return
     */
    Integer getCountByTime(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}




