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

package com.ze.pigSale.aspect;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ze.pigSale.anno.PermissionAnno;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.constants.ExceptionConstants;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.enums.PermissionEnum;
import com.ze.pigSale.service.UserPermissionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zeb
 * @Date 2023-08-10 15:31
 */
@Aspect
@Component
public class PermissionAspect {

    @Autowired
    private UserPermissionService userPermissionService;

    /**
     * 采用注解方式的切入点
     */
    @Pointcut("@annotation(com.ze.pigSale.anno.PermissionAnno)")
    public void anoPoint() {
    }

    @Around("anoPoint()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        // 签名
        Signature signature = point.getSignature();
        // 转方法签名
        MethodSignature methodSignature = (MethodSignature) signature;
        // 得到方法
        Method method = methodSignature.getMethod();

        Object proceed = point.proceed();

        // 获取对应的注解
        PermissionAnno annotation = method.getAnnotation(PermissionAnno.class);
        if (annotation == null) {
            throw new CustomException(ExceptionConstants.NOT_PERMISSION);
        }

        // 如果有这个注解 拿这个注解的值
        PermissionEnum permissionEnum = annotation.value();

        if (permissionEnum == PermissionEnum.EDIT_USER) {
            Object arg = point.getArgs()[0];
            User user = (User) arg;
            if (user.getUserId().equals(BaseContext.getCurrentId())) {
                return proceed;
            }
        }

        if (!userPermissionService.hasPermission(permissionEnum)) {
            throw new CustomException(ExceptionConstants.NOT_PERMISSION);
        }
        return proceed;
    }
}
