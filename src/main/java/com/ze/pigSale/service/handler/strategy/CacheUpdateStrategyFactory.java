package com.ze.pigSale.service.handler.strategy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zeb
 * @Date 2024-01-29 23:00
 */
@Component
public class CacheUpdateStrategyFactory implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, CacheUpdateHandler> cacheUpdateHandlerMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, CacheUpdateHandler> beans = applicationContext.getBeansOfType(CacheUpdateHandler.class);
        for (CacheUpdateHandler handler : beans.values()) {
            cacheUpdateHandlerMap.put(handler.mark(), handler);
        }
    }

    public CacheUpdateHandler chooseStrategy(String mark) {
        return cacheUpdateHandlerMap.get(mark);
    }

}
