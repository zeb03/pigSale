package com.ze.pigSale.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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

    @TableId
    private Long cartId;

    @TableField("user_id")
    private Long userId;

    private Long productId;

    private Integer quantity;

    private String name;

    private String image;

    private LocalDateTime createTime;

    private BigDecimal amount;

    private static final long serialVersionUID = 1L;

}