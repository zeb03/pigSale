package com.ze.pigSale.mapper;

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
public interface OrdersMapper {

    /**
     * 查询范围时间内的所有订单号
     * @return
     */
    List<Orders> listByTime(@Param("ordersId") Long ordersId, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    List<Orders> list();

    /**
     * 提交订单
     * @param orders
     */
    void save(Orders orders);

}




