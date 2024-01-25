package com.ze.pigSale.mq;

import com.ze.pigSale.constants.MQConstants;
import com.ze.pigSale.service.repository.EsProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zeb
 * @Date 2024-01-24 17:24
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(topic = MQConstants.PRODUCT_ES_SYNC_TOPIC_KEY, selectorExpression = MQConstants.DELETE, consumerGroup = MQConstants.PRODUCT_ES_SYNC_CON_GROUP)
public class EsProductDeleteListener implements RocketMQListener<Long> {
    @Autowired
    private EsProductRepository esProductRepository;

    @Override
    public void onMessage(Long msg) {
        log.info("监听到删除商品消息：msg={}", msg);
        esProductRepository.deleteById(msg);
    }
}
