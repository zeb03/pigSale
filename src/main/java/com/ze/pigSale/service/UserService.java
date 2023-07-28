package com.ze.pigSale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.Result;
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
    PageInfo<User> getUserPage(Integer currentPage, Integer pageSize, Integer role,String search);

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

    Result login(User user, String code);

    Result sendCode(String phone);
}
