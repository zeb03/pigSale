package com.ze.pigSale.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 
 * @TableName cart
 */
@Data
public class Cart implements Serializable {

    private Long cartId;

    private Long userId;

    private Long productId;

    private Integer quantity;

    private String name;

    private String image;

    private LocalDateTime createTime;

    private BigDecimal amount;

    private static final long serialVersionUID = 1L;

}