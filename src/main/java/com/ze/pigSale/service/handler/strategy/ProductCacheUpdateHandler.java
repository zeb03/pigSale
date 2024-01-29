package com.ze.pigSale.service.handler.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ze.pigSale.constants.RedisConstants;
import com.ze.pigSale.dto.CanalBinlogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zeb
 * @Date 2024-01-29 23:08
 */
@Component
public class ProductCacheUpdateHandler implements CacheUpdateHandler{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void updateCache(CanalBinlogEvent message) {
        // 对数据处理
        List<Map<String, Object>> messageDataList = new ArrayList<>();
        for (int i = 0; i < message.getOld().size(); i++) {
            // message.getOld只保存了被修改的字段的旧数据列表，当此字段是stock时我们才需要更新缓存
            Map<String, Object> oldDataMap = message.getOld().get(i);
            if (oldDataMap.get("stock") != null && StrUtil.isNotBlank(oldDataMap.get("stock").toString())) {
                Map<String, Object> currentDataMap = message.getData().get(i);
                messageDataList.add(currentDataMap);
            }
        }

        if (CollUtil.isEmpty(messageDataList)) {
            return;
        }
        for (Map<String, Object> each : messageDataList) {
            String stock = each.get("stock").toString();
            String productId = each.get("product_id").toString();
            stringRedisTemplate.opsForValue().set(RedisConstants.PRODUCT_STOCK_KEY + productId, stock);
        }
    }

    @Override
    public String mark() {
        return "product";
    }
}
