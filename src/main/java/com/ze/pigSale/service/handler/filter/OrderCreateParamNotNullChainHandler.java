package com.ze.pigSale.service.handler.filter;

import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.dto.OrdersDTO;
import com.ze.pigSale.service.handler.OrderCreateChainHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 验证参数非空
 * @author zeb
 * @Date 2024-01-21 15:48
 */
@Slf4j
@Component
public class OrderCreateParamNotNullChainHandler implements OrderCreateChainHandler<OrdersDTO> {

    @Override
    public void handle(OrdersDTO requestParam) {
        log.info("开始验证参数是否为空");
        Long addressId = requestParam.getAddressId();
        if (addressId == null) {
            throw new CustomException("请选择地址");
        }
        // 获取购买的商品id
        List<Long> cartItemIds = requestParam.getCartItemIds();
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new CustomException("请选择商品");
        }

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
