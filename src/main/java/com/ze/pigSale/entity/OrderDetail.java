package com.ze.pigSale.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @TableName order_detail
 */
@Data
public class OrderDetail implements Serializable {
    private Long id;

    private Long orderId;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    private static final long serialVersionUID = 1L;
}