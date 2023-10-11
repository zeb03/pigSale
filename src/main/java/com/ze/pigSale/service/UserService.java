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
import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.dto.FeedBackDTO;
import com.ze.pigSale.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * author: zebii
 * Date: 2023-03-13-15:53
 */
public interface UserService extends IService<User> {

    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * 根据name查找用户
     * @param username
     * @return
     */
    User getUserByName(String username);

    /**
     * 新增用户
     * @param user
     */
    void register(User user);

    /**
     * 分页查询所有用户
     * @param currentPage
     * @param pageSize
     * @param role
     * @return
     */
    PageInfo<User> getUserPage(Integer currentPage, Integer pageSize, Integer role, String search);

    /**
     * 修改用户
     * @param user
     * @return
     */
    void updateUser(User user);

    /**
     * 注销用户
     * @param user
     */
    void removeUser(HttpServletRequest request, User user);

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(Long id);

    Result login(User user, String code, HttpServletRequest request);

    Result sendCode(String phone);

    Result feedback(FeedBackDTO feedBackDTO);

    Result messageBox();
}
