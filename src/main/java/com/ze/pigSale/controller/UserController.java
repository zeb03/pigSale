package com.ze.pigSale.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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
//      将页面提交的密码password进行md5加密处理
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//      根据页面提交的用户名username查询数据库
        User userResult = userService.getUserByName(user.getUsername());

//      如果没有查询到则返回登录失败结果
        if (userResult == null) {
            return Result.error("用户名错误");
        }

        if (userResult.getStatus() == 3){
            return Result.error("该用户不存在");
        }

        if (userResult.getStatus() == 2){
            return Result.error("该用户已被禁用");
        }

//      密码比对，如果不一致则返回登录失败结果
        if (!userResult.getPassword().equals(password)) {
            return Result.error("密码错误");
        }

//      登录成功，将员工id存入Session并返回登录成功结果
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
        userService.register(user);

        return Result.success("添加成功");
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public Result<String> loginOut(HttpServletRequest request) {
        //清理Session中保存的当前用户登录的id
        request.getSession().removeAttribute("user");
        BaseContext.removeThreadLocal();
        return Result.success("退出成功");
    }

    @DeleteMapping("/remove")
    public Result<String> remove(@RequestBody User user, HttpServletRequest request) {
        User oneUser = userService.getUserById(user.getUserId());
        if (oneUser == null){
            throw new CustomException("用户id错误");
        }

        //修改用户状态
        oneUser.setStatus(3);
        userService.updateUser(oneUser);

        //清除数据
        request.getSession().removeAttribute("user");
        BaseContext.removeThreadLocal();
        return Result.success("成功注销");
    }

    /**
     * 管理员注册
     *
     * @param user
     * @return
     */
    @PostMapping
    public Result<User> addAdmin(@RequestBody User user) {
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
     * 查看用户信息
     *
     * @param
     * @return
     */
    @GetMapping()
    public Result<User> queryUser() {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getUserById(userId);
        return Result.success(user);
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @PutMapping
    public Result<User> editUser(@RequestBody User user) {
        Long currentId = BaseContext.getCurrentId();
        User currentUser = userService.getUserById(currentId);

        if (currentUser == null) {
            return Result.error("当前用户没有登录");
        }

        if (!"admin".equals(currentUser.getUsername()) && !Objects.equals(currentUser.getUserId(), user.getUserId())) {
            return Result.error("当前用户没有权限");
        }

        userService.updateUser(user);
        return Result.success(user);
    }


}
