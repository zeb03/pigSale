/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ze.pigSale.mq.comsumer;

import cn.hutool.core.collection.CollUtil;
import com.ze.pigSale.dto.CanalBinlogEvent;
import com.ze.pigSale.constants.MQConstants;
import com.ze.pigSale.service.handler.strategy.CacheUpdateHandler;
import com.ze.pigSale.service.handler.strategy.CacheUpdateStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 监听数据库修改更新缓存
 * @author zeb
 * @Date 2024-01-24 14:47
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(topic = MQConstants.CANAL_STOCK_TOPIC_KEY, consumerGroup = MQConstants.CANAL_STOCK_CON_GROUP)
public class CanalDataSyncBinlogConsumer implements RocketMQListener<CanalBinlogEvent> {

    private final CacheUpdateStrategyFactory cacheUpdateStrategyFactory;

    @Override
    public void onMessage(CanalBinlogEvent message) {
        // 如果是 DDL 返回
        // 如果不是 UPDATE 类型数据变更返回
        if (message.getIsDdl()
                || CollUtil.isEmpty(message.getOld())
                || !Objects.equals("UPDATE", message.getType())) {
            return;
        }

        CacheUpdateHandler strategy = cacheUpdateStrategyFactory.chooseStrategy(message.getTable());
        if (strategy != null) {
            log.info("choose strategy:{}", strategy);
            strategy.updateCache(message);
        }
    }
}
