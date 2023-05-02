package com.ze.pigSale.dto;

import com.ze.pigSale.entity.Product;
import lombok.Data;

/**
 * @author: ze
 * @Date: 2023-04-16-18:52
 */
@Data
public class ProductDto{

    private Integer currentPage;

    private Integer pageSize;

    private Long productId;
}
