package com.ze.pigSale.entity;

import lombok.Data;

import java.io.Serializable;

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

    private static final long serialVersionUID = 1L;

}