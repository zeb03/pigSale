package com.ze.pigSale.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName orders
 */
@Data
public class Orders implements Serializable {

    private Long id;

    private Long userId;

    private Long addressId;

    private BigDecimal totalPrice;

    private String state;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}