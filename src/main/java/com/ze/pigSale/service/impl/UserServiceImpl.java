package com.ze.pigSale.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.mapper.UserMapper;
import com.ze.pigSale.service.UserPermissionService;
import com.ze.pigSale.service.UserService;
import com.ze.pigSale.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * author: zebii
 * Date: 2023-03-13-15:54
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPermissionService userPermissionService;

    @Autowired
    private UserService userService;

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
        log.info("注册信息：{}", user);

        User userResult = userMapper.getUserByUsernameOrPhone(user);
        if (userResult != null) {
            throw new CustomException("用户名或手机号已被注册！");
        }

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userMapper.insertUser(user);
    }

    @Override
    public PageInfo<User> getUserPage(Integer currentPage, Integer pageSize, Integer role, String search) {
        //判断权限
        // TODO:暂时修改
//        boolean hasPermission = userPermissionService.hasPermission("view_user");
//        if (!hasPermission) {
//            throw new CustomException(CommonUtil.NOT_PERMISSION);
//        }

        PageMethod.startPage(currentPage, pageSize);
        //待完善
        List<User> userList = null;
        if (role == 0) {
            userList = userMapper.getUserByRole(search);
        } else {
            userList = userMapper.getAdminByRole(search);
        }
        return new PageInfo<>(userList);
    }

    @Override
    public void updateUser(User user) {
        //判断权限
        // TODO
//        Long userId = BaseContext.getCurrentId();
//        if (!Objects.equals(user.getUserId(), userId)) {
//            boolean hasPermission = userPermissionService.hasPermission("edit_user");
//            if (!hasPermission) {
//                throw new CustomException(CommonUtil.NOT_PERMISSION);
//            }
//        }

        if (user.getPassword() == null || "".equals(user.getPassword())) {
            userMapper.updateUser(user);
        } else {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userMapper.updateUserWithPwd(user);
        }
    }

    @Override
    public void deleteUser(HttpServletRequest request, User user) {
        User oneUser = userService.getUserById(user.getUserId());
        if (oneUser == null) {
            throw new CustomException("用户id错误");
        }

        //修改用户状态
        oneUser.setStatus(3);
        userService.updateUser(oneUser);

        //清除数据
        request.getSession().removeAttribute("user");
        BaseContext.removeThreadLocal();
    }

}
