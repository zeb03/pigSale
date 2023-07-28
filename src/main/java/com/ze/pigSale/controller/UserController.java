package com.ze.pigSale.controller;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Permissions;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;
import com.ze.pigSale.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zeb
 * Date: 2023-03-13-16:13
 */
@Slf4j
@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "*")
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

        if (userResult.getStatus() == 3) {
            return Result.error("该用户不存在");
        }

        if (userResult.getStatus() == 2) {
            return Result.error("该用户已被禁用");
        }

//      密码比对，如果不一致则返回登录失败结果
        if (!userResult.getPassword().equals(password)) {
            return Result.error("密码错误");
        }

//      登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().getServletContext().setAttribute("user", userResult.getUserId());
        log.info("登录成功:" + userResult.getUserId());
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
        request.getSession().getServletContext().removeAttribute("user");
        BaseContext.removeThreadLocal();
        return Result.success("退出成功");
    }

    /**
     * 移除用户
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

}
