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

package com.ze.pigSale.service.handler.filter;

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

    public void handle(T requestParam) {
        orderCreateChainHandlerContainer.stream().sorted(Comparator.comparing(Ordered::getOrder)).forEach(each -> each.handle(requestParam));
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, OrderCreateChainHandler> beans = applicationContext.getBeansOfType(OrderCreateChainHandler.class);
        beans.forEach((beanName, bean) -> orderCreateChainHandlerContainer.add(bean));
    }
}
