package com.ze.pigSale.dto;

import com.ze.pigSale.entity.OrderDetail;
import com.ze.pigSale.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-04-20:28
 */
@Data
public class OrdersDTO extends Orders {
    private List<OrderDetail> orderDetails;
    private List<Long> cartItemIds;
}
