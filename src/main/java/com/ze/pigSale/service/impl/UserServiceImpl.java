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

package com.ze.pigSale.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.anno.PermissionAnno;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.dto.FeedBackDTO;
import com.ze.pigSale.dto.UserDTO;
import com.ze.pigSale.entity.Orders;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;
import com.ze.pigSale.enums.PermissionEnum;
import com.ze.pigSale.mapper.UserMapper;
import com.ze.pigSale.service.OrdersService;
import com.ze.pigSale.service.ProductService;
import com.ze.pigSale.service.UserPermissionService;
import com.ze.pigSale.service.UserService;
import com.ze.pigSale.utils.RegexUtils;
import com.ze.pigSale.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ze.pigSale.constants.RedisConstants.*;
import static com.ze.pigSale.enums.PermissionEnum.EDIT_USER;
import static com.ze.pigSale.enums.PermissionEnum.HANDLE_FEEDBACK;
import static com.ze.pigSale.utils.UserReuseUtil.hashShardingIdx;

/**
 * author: zebii
 * Date: 2023-03-13-15:54
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private UserPermissionService userPermissionService;
    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ProductService productService;
    @Resource
    private OrdersService ordersService;
    @Resource
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    public UserServiceImpl(RBloomFilter<String> userRegisterCachePenetrationBloomFilter) {
        this.userRegisterCachePenetrationBloomFilter = userRegisterCachePenetrationBloomFilter;
    }

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
        SnowFlake snowFlake = new SnowFlake(0, 0);
        long id = snowFlake.nextId();
        user.setUserId(id);
        // 设置初始状态
        user.setRole(0);
        user.setStatus(1);
        user.setImage("avatar.jpg");
        // 使用布隆过滤器防止缓存穿透
        String username = user.getUsername();
        boolean hasUserName = userRegisterCachePenetrationBloomFilter.contains(username);
        //此处可能存在误判，但是如果查询不到，则数据库一定是没有的
        if (hasUserName) {
            //查询缓存判断该用户名是否已经注销，注销用户名采用分片存储
            Boolean isReused = stringRedisTemplate.opsForSet().isMember(USER_REGISTER_REUSE_SHARDING_KEY + hashShardingIdx(username), username);
            if (Boolean.FALSE.equals(isReused)) {
                throw new CustomException("用户名或手机号已被注册！");
            }
        }
        log.info("注册用户验证通过");
//        User userResult = userMapper.getUserByUsernameOrPhone(user);
//        if (userResult != null) {
//            throw new CustomException("用户名或手机号已被注册！");
//        }
//        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        try {
            userMapper.insertUser(user);
        } catch (DuplicateKeyException dke) {
            log.error("用户名或手机号重复注册");
            throw new CustomException("用户名或手机号已经被注册");
        }
        //删除可复用表的数据
        stringRedisTemplate.opsForSet().remove(USER_REGISTER_REUSE_SHARDING_KEY + hashShardingIdx(username), username);
        //添加数据到布隆过滤器
        // 布隆过滤器设计问题：设置多大、碰撞率以及初始容量不够了怎么办？详情查看：https://nageoffer.com/12306/question
        userRegisterCachePenetrationBloomFilter.add(username);
    }

    @Override
//    @PermissionAnno(value = PermissionEnum.VIEW_USER)
    public PageInfo<User> getUserPage(Integer currentPage, Integer pageSize, Integer role, String search) {
        PageMethod.startPage(currentPage, pageSize);
        List<User> userList = null;
        if (role == 0) {
            userList = userMapper.getUserByRole(search);
        } else {
            userList = userMapper.getAdminByRole(search);
        }
        return new PageInfo<>(userList);
    }

    @Override
//    @PermissionAnno(value = EDIT_USER)
    public void updateUser(User user) {
        if (user.getPassword() == null || "".equals(user.getPassword())) {
            userMapper.updateUser(user);
        } else {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            userMapper.updateUserWithPwd(user);
        }
    }

    @Override
    public void removeUser(HttpServletRequest request, User user) {
        User oneUser = userService.getUserById(user.getUserId());
        if (oneUser == null) {
            throw new CustomException("用户id错误");
        }

        // 修改用户状态
        oneUser.setStatus(3);
        userService.deleteUser(oneUser.getUserId());

        // 清除数据
        request.getSession().removeAttribute("user");
        BaseContext.removeThreadLocal();
        String username = user.getUsername();
        stringRedisTemplate.opsForSet().add(USER_REGISTER_REUSE_SHARDING_KEY + hashShardingIdx(username), username);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteUser(id);
    }

    @Override
    public Result sendCode(String phone) {
        // 1.校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.error("手机号格式错误！");
        }
        // 3.符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        // 4.保存验证码到redis
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        // 5.发送验证码
        log.info("发送短信验证码成功，验证码：{}", code);
        // 返回ok
        return Result.success("发送验证码成功");
    }

    @Override
    public Result feedback(FeedBackDTO feedBackDTO) {
        Integer permissionId = HANDLE_FEEDBACK.getPermissionId();
        LambdaQueryWrapper<UserPermissions> wrapper = new LambdaQueryWrapper<>();
        log.info("permissionId:" + permissionId);
        wrapper.eq(UserPermissions::getPermissionId, permissionId);
        List<UserPermissions> list = userPermissionService.list(wrapper);
        log.info("list" + list);
        for (UserPermissions userPermissions : list) {
            Long userId = userPermissions.getUserId();
            String key = FEED_FEEDBACK_KEY + userId;
            String jsonStr = JSONUtil.toJsonStr(feedBackDTO);
            stringRedisTemplate.opsForZSet().add(key, jsonStr, System.currentTimeMillis());
        }
        return Result.success("反馈成功");
    }

    @Override
    public Result messageBox() {
        // 获取当前用户
        Long userId = BaseContext.getCurrentId();
        // 根据用户id，查找redis缓存
        String key = "feed:*" + userId;
        log.info("key: " + key);
        // 使用通配符匹配，获取用户收件箱
        Set<String> keys = stringRedisTemplate.keys(key);
        // 为空则直接返回
        if (keys == null || keys.isEmpty()) {
            return Result.success(Collections.emptyList());
        }
        // 创建list
        List<Product> productList = new ArrayList<>();
        List<Orders> orderList = new ArrayList<>();
        List<FeedBackDTO> feedBackList = new ArrayList<>();
        // 遍历所有消息
        for (String cacheKey : keys) {
            Set<ZSetOperations.TypedTuple<String>> typedTuples =
                    stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(cacheKey, 0, System.currentTimeMillis());
            if (typedTuples == null) {
                continue;
            }
            log.info("cache key:" + cacheKey);
            // 将收件箱进行分类
            if (cacheKey.contains(FEED_SHOP_KEY)) {
                // 收藏商品变更消息
                productList = typedTuples.stream().map(item -> {
                    Long productId = Long.valueOf(Objects.requireNonNull(item.getValue()));
                    return productService.getProductById(productId);
                }).collect(Collectors.toList());
            } else if (cacheKey.contains(FEED_ORDER_KEY)) {
                // 订单申请消息
                orderList = typedTuples.stream().map(item -> {
                    Long orderId = Long.valueOf(Objects.requireNonNull(item.getValue()));
                    log.info("order:" + orderId);
                    Orders orders = ordersService.getById(orderId);
                    log.info("orders:" + orders);
                    return orders;
                }).collect(Collectors.toList());
            } else {
                // 用户反馈消息
                feedBackList = typedTuples.stream().map(item -> {
                    String value = item.getValue();
                    return JSONUtil.toBean(value, FeedBackDTO.class);
                }).collect(Collectors.toList());
            }
        }
        // 方案一：返回一个map
        // 方案二：写多个接口，多次返回
        HashMap<String, List> map = new HashMap<>(3);
        map.put("shop", productList);
        map.put("order", orderList);
        map.put("feedback", feedBackList);
        return Result.success(map);
    }

    @Override
    public Result login(User user, String code, HttpServletRequest request) {
        // 判断登录方式
        if (code != null && !code.isEmpty()) {
            // 验证码登录
            return loginByPhone(user, code);
        }

        // 用户名+密码登录
        return loginByPasswd(user, request);
    }

    private Result<?> loginByPasswd(User user, HttpServletRequest request) {
        // 将页面提交的密码password进行md5加密处理
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 根据页面提交的用户名username查询数据库
        User userResult = userService.getUserByName(user.getUsername());

        // 如果没有查询到则返回登录失败结果
        if (userResult == null) {
            return Result.error("用户名错误");
        }
        if (userResult.getStatus().equals(USER_NOT_EXISTS)) {
            return Result.error("该用户不存在");
        }
        if (userResult.getStatus().equals(USER_DISABLED)) {
            return Result.error("该用户已被禁用");
        }

        // 密码比对，如果不一致则返回登录失败结果
        if (!userResult.getPassword().equals(password)) {
            return Result.error("密码错误");
        }

        request.getSession().getServletContext().setAttribute("user", userResult.getUserId());
        return Result.success(userResult);
    }

    private Result<?> loginByPhone(User user, String code) {
        // 1.校验手机号
        String phone = user.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.error("手机号格式错误！");
        }
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致，报错
            return Result.error("验证码错误");
        }
        // 根据手机号查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User one = this.getOne(wrapper);
        if (one == null) {
            // 自动注册用户
            String username = DEFAULT_USERNAME + RandomUtil.randomString(6);
            user.setUsername(username);
            this.register(user);
        } else if (one.getStatus().equals(USER_NOT_EXISTS)) {
            return Result.error("该用户不存在");
        } else if (one.getStatus().equals(USER_DISABLED)) {
            return Result.error("该用户已被禁用");
        }

        // 保存到redis
        String token = UUID.randomUUID().toString();
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> map = BeanUtil.beanToMap(userDTO, new HashMap<>(), CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + token, map);
        // 设置token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 返回token
        return Result.success(token);
    }

}
