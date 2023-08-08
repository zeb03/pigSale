package com.ze.pigSale.dto;

import lombok.Data;

/**
 * @author: ze
 * @Date: 2023-04-16-18:52
 */
@Data
public class ProductDTO {

    private Integer currentPage;

    private Integer pageSize;

    private Long productId;
}
