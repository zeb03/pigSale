package com.ze.pigSale.mq;

import com.ze.pigSale.entity.EsProduct;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.repository.EsProductRepository;
import com.ze.pigSale.service.EsProductService;
import com.ze.pigSale.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ze.pigSale.constants.MqConstants.*;

/**
 * @author zeb
 * @Date 2023-08-07 22:18
 * 监听数据库的增删改，将数据同步到es搜索引擎
 */
@Component
@Slf4j
public class EsProductListener {

    @Autowired
    private EsProductRepository esProductRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private EsProductService esProductService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = INSERT_QUEUE_NAME),
            exchange = @Exchange(name = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = INSERT_KEY))
    public void listenProductInsert(Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            EsProduct esProduct = esProductService.getEsProduct(product);
            log.info("esProduct" + esProduct);
            esProductRepository.save(esProduct);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = DELETE_QUEUE_NAME),
            exchange = @Exchange(name = EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = DELETE_KEY
    ))
    public void listenHotelDelete(Long productId) {
        // 删除
        esProductRepository.deleteById(productId);
    }

}
