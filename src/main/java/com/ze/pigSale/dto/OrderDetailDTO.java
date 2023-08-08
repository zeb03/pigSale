package com.ze.pigSale.dto;

import com.ze.pigSale.entity.OrderDetail;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zeb
 * @Date 2023-06-04 16:53
 */

@Data
public class OrderDetailDTO extends OrderDetail {
    private LocalDateTime checkoutTime;
}
