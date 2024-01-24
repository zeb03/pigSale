package com.ze.pigSale.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 责任链容器
 * @author zeb
 * @Date 2024-01-21 15:40
 */
@Component
public class OrderCreateChainContext<T> implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    private final List<OrderCreateChainHandler> orderCreateChainHandlerContainer = new ArrayList<>();

    public void handle(T requestParam){
        orderCreateChainHandlerContainer.stream().sorted(Comparator.comparing(Ordered::getOrder)).forEach(each -> each.handle(requestParam));
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, OrderCreateChainHandler> beans = applicationContext.getBeansOfType(OrderCreateChainHandler.class);
        beans.forEach((beanName, bean) -> orderCreateChainHandlerContainer.add(bean));
    }
}
