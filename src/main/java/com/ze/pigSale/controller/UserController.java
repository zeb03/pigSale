package com.ze.pigSale.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * author: zebii
 * Date: 2023-03-13-16:13
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user, HttpServletRequest request) {
//      1、将页面提交的密码password进行md5加密处理
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//      2、根据页面提交的用户名username查询数据库
        User userResult = userService.getUserByName(user.getUsername());

//      3、如果没有查询到则返回登录失败结果
        if (userResult == null) {
            return Result.error("用户名错误");
        }

//      4、密码比对，如果不一致则返回登录失败结果
        if (!userResult.getPassword().equals(password)) {
            return Result.error("密码错误");
        }

//      5、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("user", userResult.getUserId());
        return Result.success(userResult);
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        user.setRole(0);
        userService.register(user);

        return Result.success("添加成功");
    }


    /**
     * 管理员注册
     *
     * @param user
     * @return
     */
    @PostMapping
    public Result<User> add(@RequestBody User user) {
        user.setRole(1);
        userService.register(user);
        return Result.success(user);
    }


    /**
     * 分页查询所有用户
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<PageInfo<User>> page(Integer currentPage, Integer pageSize, Integer role) {
        PageInfo<User> userPage = userService.getUserPage(currentPage, pageSize, role);
        return Result.success(userPage);
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @PutMapping
    public Result<User> edit(@RequestBody User user, Long updateUser) {
        User role = userService.getUserById(updateUser);
        if ("admin".equals(role.getUsername())) {
            userService.updateUser(user);
            return Result.success(user);
        } else {
            return Result.error("无权限");
        }
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public Result<String> remove(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return Result.success("删除成功");
    }
}
