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

package com.ze.pigSale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;
import com.ze.pigSale.enums.PermissionEnum;

import java.util.List;

/**
 * Date: 2023-04-09-16:09
 * @author ze
 */
public interface UserPermissionService extends IService<UserPermissions> {

    /**
     * 获取用户权限
     * @param userId
     * @return
     */
    List<UserPermissions> getByUserId(Long userId);

    /**
     * 判断用户是否具有权限
     *
     * @param permissionEnum
     * @return
     */
    boolean hasPermission(PermissionEnum permissionEnum);

    /**
     * 赋予管理员权限
     * @param userPermissions
     */
    void addPermission(UserPermissions userPermissions);

    /**
     * 批量赋予管理员权限
     * @param userPermissions
     */
    void addBatchPermission(List<UserPermissions> userPermissions);

    /**
     * 根据id删除用户权限
     * @param id
     */
    void deletePermissionById(Long id);

    /**
     * 修改用户权限
     * @param userPermissions
     */
    void updatePermission(List<UserPermissions> userPermissions);

    /**
     * 删除用户所有权限
     * @param userId
     */
    void deleteByUser(Long userId);
}
