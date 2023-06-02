package com.ze.pigSale.vo;

import com.ze.pigSale.entity.Review;
import lombok.Data;

/**
 * @author: ze
 * @Date: 2023-04-18-22:41
 */
@Data
public class ReviewVo extends Review {
    private String avatar;
    private String productName;
    private String productImage;
    private String description;
}
