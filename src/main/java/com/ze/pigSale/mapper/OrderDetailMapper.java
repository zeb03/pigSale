package com.ze.pigSale.mapper;

import com.ze.pigSale.dto.OrderDetailDto;
import com.ze.pigSale.entity.OrderDetail;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.vo.OrderDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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

    /**
     * 获取已经送达但是没有评价的订单
     * @param userId
     * @return
     */
    List<OrderDetailVo> getByNoReview(@Param("userId") Long userId);

    /**
     * 获取最近的销量
     */
    List<OrderDetailDto> getListByTime(@Param("now") LocalDateTime now, @Param("time") LocalDateTime time);


}




