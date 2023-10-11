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

package com.ze.pigSale.interceptor;

import com.alibaba.fastjson.JSON;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date: 2023-01-28-20:35
 *
 * @author ze
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 打印请求信息
        log.info("------------- LoginInterceptor 开始 -------------");
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        // OPTIONS请求不做校验,
        // 前后端分离的架构, 前端会发一个OPTIONS请求先做预检, 对预检请求不做校验
        if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }

        String path = request.getRequestURL().toString();
        log.info("接口登录拦截：，path：{}", path);

        Long user = (Long) request.getSession().getServletContext().getAttribute("user");
        if (user != null) {
            log.info("用户id:" + user);
            BaseContext.setCurrentId(user);
            return true;
        }
        log.info("用户未登录");
        // response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(JSON.toJSONString(Result.error(401, "not login")));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("requestStartTime");
        log.info("------------- LoginInterceptor 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
    }
}
