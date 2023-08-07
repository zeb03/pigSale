package com.ze.pigSale.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName user_product
 */
@Data
public class UserProduct implements Serializable {
    private Long id;

    private Long userId;

    private Long productId;
}