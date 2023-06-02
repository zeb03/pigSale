package com.ze.pigSale.vo;

import com.ze.pigSale.entity.Product;
import com.ze.pigSale.entity.Review;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Date: 2023-03-15-22:38
 * @author ze
 */
@Data
@ToString
public class ProductVo extends Product {

    Integer sales;

    private String categoryName;

    private Double rating;
}
