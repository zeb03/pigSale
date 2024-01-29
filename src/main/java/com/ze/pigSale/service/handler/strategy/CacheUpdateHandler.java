package com.ze.pigSale.service.handler.strategy;

import com.ze.pigSale.dto.CanalBinlogEvent;
import org.springframework.stereotype.Component;

/**
 * @author zeb
 * @Date 2024-01-29 22:59
 */
@Component
public interface CacheUpdateHandler {

    void updateCache(CanalBinlogEvent message);

    String mark();
}
