package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【order_detail】的数据库操作Mapper
* @createDate 2023-04-04 20:23:19
* @Entity com.ze.pigSale.entity.OrderDetail
*/
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量保存
     * @param orderDetails
     */
    void saveBatch(List<OrderDetail> orderDetails);

    /**
     * 获取某订单详情
     * @param orderId
     * @return
     */
    List<OrderDetail> getListByOrderId(@Param("orderId") Long orderId);
}




