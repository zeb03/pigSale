package com.ze.pigSale.service;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.entity.User;


/**
 * author: zebii
 * Date: 2023-03-13-15:53
 */
public interface UserService {
    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    User getUserById(Integer id);

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
     */
    PageInfo<User> getUserPage(Integer currentPage, Integer pageSize);

    /**
     * 修改用户
     * @param user
     * @return
     */
    void updateUser(User user);

    /**
     * 删除用户
     * @param userId
     */
    void deleteUser(Long userId);
}
