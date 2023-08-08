package com.ze.pigSale.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zeb
 * @Date 2023-08-08 16:05
 */
@Data
public class OrderMQ {
    private Long orderId;
    private Long userId;
    private List<Long> cartId;
    private Long address;
}
