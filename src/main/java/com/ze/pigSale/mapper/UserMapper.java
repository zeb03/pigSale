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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ze.pigSale.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-03-13 15:34:09
* @Entity com.ze.pig.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过id查找用户
     * @param id
     * @return
     */
    User getUserById(@Param("id") Long id);

    /**
     * 通过name查找用户
     * @param username
     * @return
     */
    User getUserByName(@Param("name") String username);

    /**
     * 通过role查找用户
     * @param search
     * @return
     */
    List<User> getUserByRole(@Param("search") String search);

    /**
     * 通过role查找用户
     * @param search
     * @return
     */
    List<User> getAdminByRole(@Param("search") String search);

    /**
     * 查询所有用户
     * @return
     */
    List<User> getUserList();

    /**
     * 保存用户
     * @param user
     */
    void insertUser(User user);

    /**
     * 通过phone或username查找用户
     * @param user
     * @return
     */
    User getUserByUsernameOrPhone(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    void updateUser(User user);

    /**
     * 修改用户和密码
     * @param user
     */
    void updateUserWithPwd(User user);

    /**
     * 删除用户
     * @param userId
     */
    void deleteUser(@Param("userId") Long userId);
}
