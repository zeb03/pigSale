package com.ze.pigSale.vo;

import com.ze.pigSale.entity.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: ze
 * @Date: 2023-04-19-13:14
 */
@Data
public class OrderDetailVo extends OrderDetail {
    private LocalDateTime checkoutTime;
    private String image;
    private String productName;
    private String description;
    private BigDecimal price;
}
