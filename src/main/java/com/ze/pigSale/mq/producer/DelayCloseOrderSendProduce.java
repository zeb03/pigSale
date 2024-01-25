package com.ze.pigSale.mq.producer;

import com.ze.pigSale.dto.OrdersDTO;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static com.ze.pigSale.constants.MQConstants.ORDER_DELAY_CLOSE_TOPIC_KEY;

/**
 * @author zeb
 * @Date 2024-01-25 14:59
 */
@Component
public class DelayCloseOrderSendProduce {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    private static final String topic = ORDER_DELAY_CLOSE_TOPIC_KEY;

    /**
     * 发送延时消息（上面的发送同步消息，delayLevel的值就为0，因为不延时）
     * 在start版本中 延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    public void sendDelayMsg(OrdersDTO msgBody, int delayLevel) {
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(msgBody).build(), 2000L, delayLevel);
    }

}
