package com.ze.pigSale.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @TableName product
 */
@Data
public class Product implements Serializable {

    private Long productId;

    private String productName;

    private String description;

    private Long categoryId;

    private BigDecimal price;

    private String image;

    private String origin;

    private Integer stock;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;

}