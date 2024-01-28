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

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.dto.FeedBackDTO;
import com.ze.pigSale.entity.Permissions;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;
import com.ze.pigSale.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zeb
 * Date: 2023-03-13-16:13
 */
@Slf4j
@RestController
@RequestMapping("/user")
// @CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserPermissionService userPermissionService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ReviewService reviewService;

    /**
     * 查询用户收件箱
     *
     * @return
     */
    @GetMapping("/msg/box")
    public Result queryMessageBox() {
        return userService.messageBox();
    }

    /**
     * 用户反馈
     *
     * @param feedBackDTO
     * @return
     */
    @PostMapping("/feedback")
    public Result feedback(@RequestBody FeedBackDTO feedBackDTO) {
        return userService.feedback(feedBackDTO);
    }

    /**
     * 发送手机验证码
     */
    @PostMapping("/code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        // 发送短信验证码并保存验证码
        return userService.sendCode(phone);
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user, @RequestParam(required = false) String code, HttpServletRequest request) {
        return userService.login(user, code, request);
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
        // 清理Session中保存的当前用户登录的id
        request.getSession().getServletContext().removeAttribute("user");
        BaseContext.removeThreadLocal();
        return Result.success("退出成功");
    }

    /**
     * 注销用户
     *
     * @param user
     * @param request
     * @return
     */
    @DeleteMapping("/remove")
    public Result<String> remove(@RequestBody User user, HttpServletRequest request) {
        userService.removeUser(request, user);
        return Result.success("成功注销");
    }

    /**
     * 添加管理员
     * 待完善，可以给管理员各种权限
     *
     * @param user
     * @return
     */
    @PostMapping
    @Transactional
    public Result<User> addAdmin(@RequestBody User user) {
        log.info("user:" + user);
        user.setStatus(1);
        if (BaseContext.getCurrentId() != 1) {
            throw new CustomException("此用户无权限");
        }
        userService.register(user);
        List<Permissions> permissions = permissionService.getByRoleId(user.getRole());
        permissions.stream().map(item -> {
            UserPermissions userPermissions = new UserPermissions();
            BeanUtils.copyProperties(item, userPermissions);
            userPermissions.setUserId(user.getUserId());
            userPermissionService.addPermission(userPermissions);
            return item;
        }).collect(Collectors.toList());
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
    public Result<PageInfo<User>> page(Integer currentPage, Integer pageSize, Integer role, String search) {
        log.info(search);
        PageInfo<User> userPage = userService.getUserPage(currentPage, pageSize, role, search);
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
        log.info("修改用户信息: " + user);
        userService.updateUser(user);
        return Result.success(user);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteUser(@PathVariable("id") Long id) {
        cartService.cleanCart(id);
        reviewService.deleteByUser(id);
        userService.deleteUser(id);
        userPermissionService.deleteByUser(id);
        return Result.success("删除成功");
    }

//    @GetMapping("/clean")
//    public Result<String> cleanUser() {
//        PageInfo<User> userPage = userService.getUserPage(1, 100, 0, "");
//        List<User> list = userPage.getList();
//        list.stream().map(item -> {
//            item.setPassword(null);
//            userService.updateUser(item);
//            return null;
//        }).collect(Collectors.toList());
//        return Result.success("清洗成功");
//    }
}
