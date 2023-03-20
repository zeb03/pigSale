package com.ze.pigSale.dto;

import com.ze.pigSale.entity.Product;
import lombok.Data;

/**
 * author: zebii
 * Date: 2023-03-15-22:38
 */
@Data
public class ProductDto extends Product {

    private String categoryName;
}
