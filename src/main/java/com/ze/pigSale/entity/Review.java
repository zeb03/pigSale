package com.ze.pigSale.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * @TableName review
 */
@Data
public class Review implements Serializable {
    private Long reviewId;

    private Long userId;

    private Long productId;

    private Integer rating;

    private String comment;

    private String image;

    private Integer anonymous;

    private LocalDateTime publishTime;

    private String username;

    private static final long serialVersionUID = 1L;
}