package com.ze.pigSale.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * @TableName orders
 */
@Data
public class Orders implements Serializable {
    private Long id;

    private Long userId;

    private Long addressId;

    private BigDecimal totalPrice;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime checkoutTime;

    private Integer payMethod;

    private String remark;

    private String phone;

    private String address;

    private String userName;

    private static final long serialVersionUID = 1L;
}