package com.ze.pigSale.mq;

import com.ze.pigSale.constants.MQConstants;
import com.ze.pigSale.entity.EsProduct;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.service.repository.EsProductRepository;
import com.ze.pigSale.service.EsProductService;
import com.ze.pigSale.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zeb
 * @Date 2024-01-24 17:23
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(topic = MQConstants.PRODUCT_NAME, selectorExpression = MQConstants.INSERT, consumerGroup = MQConstants.PRODUCT_CON_GROUP)
public class EsProductInsertListener implements RocketMQListener<Long> {
    @Autowired
    private EsProductRepository esProductRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private EsProductService esProductService;

    // 监听到消息就会执行此方法
    @Override
    public void onMessage(Long msg) {
        log.info("监听到数据库插入商品消息：msg={}", msg);
        Product product = productService.getProductById(msg);
        if (product != null) {
            EsProduct esProduct = esProductService.getEsProduct(product);
            log.info("esProduct" + esProduct);
            esProductRepository.save(esProduct);
        }
    }
}

