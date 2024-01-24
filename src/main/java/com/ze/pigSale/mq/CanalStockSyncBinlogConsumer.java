package com.ze.pigSale.mq;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ze.pigSale.canal.CanalBinlogEvent;
import com.ze.pigSale.constants.MQConstants;
import com.ze.pigSale.constants.RedisConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zeb
 * @Date 2024-01-24 14:47
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = MQConstants.CANAL_STOCK_NAME,
        consumerGroup = MQConstants.CANAL_CONSUMER_GROUP
)
public class CanalStockSyncBinlogConsumer implements RocketMQListener<CanalBinlogEvent> {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(CanalBinlogEvent message) {
        // 如果是 DDL 返回
        // 如果不是 UPDATE 类型数据变更返回
        // 如果不是修改product表返回
        if (message.getIsDdl()
                || CollUtil.isEmpty(message.getOld())
                || !Objects.equals("UPDATE", message.getType())
                || !message.getTable().equals("product")) {
            return;
        }

        log.info("binlog_message:{}", message);

        //对数据处理
        List<Map<String, Object>> messageDataList = new ArrayList<>();
        for (int i = 0; i < message.getOld().size(); i++) {
            //message.getOld只保存了被修改的字段的旧数据列表，当此字段是stock时我们才需要更新缓存
            Map<String, Object> oldDataMap = message.getOld().get(i);
            if (oldDataMap.get("stock") != null && StrUtil.isNotBlank(oldDataMap.get("stock").toString())) {
                Map<String, Object> currentDataMap = message.getData().get(i);
                messageDataList.add(currentDataMap);
            }
        }

        if (CollUtil.isEmpty(messageDataList)) {
            return;
        }
        log.info("currentData{}", messageDataList);
        for (Map<String, Object> each : messageDataList) {
            String stock = each.get("stock").toString();
            String productId = each.get("product_id").toString();
            stringRedisTemplate.opsForValue().set(RedisConstants.PRODUCT_STOCK_KEY + productId, stock);
        }

    }
}
