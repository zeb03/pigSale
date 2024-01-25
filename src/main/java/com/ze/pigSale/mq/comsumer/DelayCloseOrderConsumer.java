package com.ze.pigSale.mq.comsumer;

import com.alibaba.fastjson.JSON;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.constants.MQConstants;
import com.ze.pigSale.dto.OrdersDTO;
import com.ze.pigSale.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zeb
 * @Date 2024-01-25 15:24
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = MQConstants.ORDER_DELAY_CLOSE_TOPIC_KEY,
        consumerGroup = MQConstants.TICKET_DELAY_CLOSE_CG_KEY
)
public class DelayCloseOrderConsumer implements RocketMQListener<OrdersDTO> {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public void onMessage(OrdersDTO ordersDTO) {
        log.info("[延迟关闭订单] 开始消费：{}", JSON.toJSONString(ordersDTO));
        boolean isSuccess = ordersService.closeOrders(ordersDTO);
        if (!isSuccess) {
            log.info("[延迟关闭订单] 订单号：{} 用户已支付订单", ordersDTO.getId());
        }
    }

}
