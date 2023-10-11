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

package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Permissions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【permissions】的数据库操作Mapper
* @createDate 2023-04-09 16:07:38
* @Entity com.ze.pigSale.entity.Permissions
*/
@Mapper
public interface PermissionsMapper {

    /**
     * 查看所有权限
     * @return
     */
    List<Permissions> getList();

    /**
     * 根据id获取
     * @param id
     * @return
     */
    Permissions getById(@Param("id") Integer id);

    /**
     * 根据role获取权限
     * @param RoleId
     * @return
     */
    List<Permissions> getByRoleId(@Param(("roleId")) Integer RoleId);
}
