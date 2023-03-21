package com.ze.pigSale.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @TableName address
 */

@Data
public class Address implements Serializable {
    private Long addressId;

    private Long userId;

    private String recipientName;

    private String recipientPhone;

    private String province;

    private String city;

    private String district;

    private String detail;

    private static final long serialVersionUID = 1L;

}