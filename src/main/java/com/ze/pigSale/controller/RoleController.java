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
import com.ze.pigSale.entity.Permissions;
import com.ze.pigSale.entity.Role;
import com.ze.pigSale.service.RoleService;
import com.ze.pigSale.vo.RoleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PipedReader;
import java.util.List;

/**
 * @author: ze
 * @Date: 2023-04-12-16:30
 */
@RestController
@Slf4j
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public Result<List<Role>> list() {
        List<Role> roles = roleService.getRoles();
        return Result.success(roles);
    }

    /**
     * 获取所有角色及其权限
     * @return
     */
    @GetMapping("/permissions")
    public Result<List<RoleVo>> listWithPermissions() {
        List<Role> roles = roleService.getRoles();
        log.info("所有角色：" + roles);
        List<RoleVo> rolesWithPermissions = roleService.getRolesWithPermissions(roles);
        log.info("所有角色及其权限：" + roles);
        return Result.success(rolesWithPermissions);
    }

}
