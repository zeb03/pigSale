package com.ze.pigSale.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class OrderIdGeneratorManager implements InitializingBean {

    private static DistributedIdGenerator DISTRIBUTED_ID_GENERATOR;

    /**
     * 生成订单全局唯一 ID
     *
     * @param userId 用户名
     * @return 订单 ID
     */
    public static String generateId(long userId) {
        return DISTRIBUTED_ID_GENERATOR.generateId() + String.valueOf(userId % 1000000);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}