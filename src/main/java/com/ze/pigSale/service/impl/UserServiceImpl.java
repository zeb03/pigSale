package com.ze.pigSale.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.mapper.UserMapper;
import com.ze.pigSale.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-13-15:54
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        User user = userMapper.getUserById(id);
        user.setPassword(null);
        return user;
    }

    @Override
    public User getUserByName(String username) {
        return userMapper.getUserByName(username);
    }


    @Override
    public void register(User user) {
        user.setRole(0);
        log.info("注册信息：{}", user);

        User userResult = userMapper.getUserByUsernameOrPhone(user);
        if (userResult != null) {
            throw new CustomException("用户名或手机号已被注册！");
        }

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        userMapper.insertUser(user);
    }

    @Override
    public PageInfo<User> getUserPage(Integer currentPage, Integer pageSize, Integer role) {
        PageMethod.startPage(currentPage, pageSize);
        List<User> userList = userMapper.getUserByRole(role);
        return new PageInfo<>(userList);
    }

    @Override
    public void updateUser(User user) {
        if (user.getPassword() == null || "".equals(user.getPassword())) {
            userMapper.updateUser(user);
        } else {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userMapper.updateUserWithPwd(user);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new CustomException("此用户不存在");
        }
        userMapper.deleteUser(userId);
    }

}
