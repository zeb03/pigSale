package com.ze.pigSale.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName orderitem
 */
@Data
public class OrderItem implements Serializable {

    private Long id;

    private Long orderId;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    private static final long serialVersionUID = 1L;

}