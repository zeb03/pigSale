package com.ze.pigSale.service.handler;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 责任链接口
 * @author zeb
 * @Date 2024-01-21 15:37
 */
@Component
public interface OrderCreateChainHandler<T> extends Ordered {
    /**
     * 执行责任链
     * @param requestParam
     */
    void handle(T requestParam);
}
