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

package com.ze.pigSale.controller;

import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.UserPermissions;
import com.ze.pigSale.service.UserPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: ze
 * @Date: 2023-04-12-15:07
 */
@RestController
@RequestMapping("/user/permissions")
@Slf4j
public class UserPermissionsController {

    @Autowired
    private UserPermissionService userPermissionService;

    /**
     * 获取某用户权限
     *
     * @return
     */
    @GetMapping("/list/{userId}")
    public Result<List<UserPermissions>> list(@PathVariable("userId") Long userId) {
        log.info("userId:" + userId);
        List<UserPermissions> permissions = userPermissionService.getByUserId(userId);
        return Result.success(permissions);
    }

    /**
     * 添加权限
     *
     * @param userPermissions
     * @return
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody UserPermissions userPermissions) {
        log.info("userPermissions:" + userPermissions);
        userPermissionService.addPermission(userPermissions);
        return Result.success("成功赋予该管理员权限");
    }

    /**
     * 批量保存
     *
     * @param userPermissions
     * @return
     */
    @PostMapping("/add/batch")
    public Result<String> addBatch(@RequestBody List<UserPermissions> userPermissions) {
        userPermissionService.updatePermission(userPermissions);
        return Result.success("成功赋予该管理员权限");
    }

    /**
     * 移除权限
     *
     * @param id
     * @return
     */
    @DeleteMapping("/remove/{id}")
    public Result<String> remove(@PathVariable("id") Long id) {
        userPermissionService.deletePermissionById(id);
        return Result.success("移除权限成功");
    }
}
