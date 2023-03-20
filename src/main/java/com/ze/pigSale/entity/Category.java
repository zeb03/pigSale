package com.ze.pigSale.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName category
 */
@Data
public class Category implements Serializable {

    private Long categoryId;

    private String categoryName;

    private Long parentId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;

}