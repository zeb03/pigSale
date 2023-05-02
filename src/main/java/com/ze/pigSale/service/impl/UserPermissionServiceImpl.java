package com.ze.pigSale.service.impl;

import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.entity.Permissions;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;
import com.ze.pigSale.mapper.UserPermissionsMapper;
import com.ze.pigSale.service.PermissionService;
import com.ze.pigSale.service.UserPermissionService;
import com.ze.pigSale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-09-16:10
 */
@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    @Autowired
    private UserPermissionsMapper userPermissionsMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<UserPermissions> getByUserId(Long userId) {
        User user = userService.getUserById(userId);
        //TODO
//        User currentUser = userService.getUserById(BaseContext.getCurrentId());
//        if (currentUser.getUserId() != 1) {
//            throw new CustomException("无权限");
//        }

        return userPermissionsMapper.getByUserId(user);
    }

    @Override
    public boolean hasPermission(String operation) {
        // TODO:暂时修改
//        Long userId = 1L;
        Long userId = BaseContext.getCurrentId();
        User user = userService.getUserById(userId);
        List<UserPermissions> permissions = this.getByUserId(user.getUserId());
        boolean hasPermission = false;
        for (UserPermissions permission : permissions) {
            if (operation.equals(permission.getPermissionId().getPermissionName())) {
                hasPermission = true;
                break;
            }
        }

        return hasPermission;
    }

    @Override
    public void addPermission(UserPermissions userPermissions) {
        // TODO:
//        Long userId = BaseContext.getCurrentId();
        Long userId = 1L;
        User user = userService.getUserById(userId);
        if (user.getUserId() != 1) {
            throw new CustomException("无权限");
        }

        userPermissions.setCreatedTime(LocalDateTime.now());
        userPermissions.setUpdatedTime(LocalDateTime.now());

        userPermissionsMapper.save(userPermissions);
    }

    @Override
    public void addBatchPermission(List<UserPermissions> userPermissions) {
        userPermissions.forEach(this::addPermission);
    }

    @Override
    public void deletePermissionById(Long id) {
        User user = userService.getUserById(BaseContext.getCurrentId());
        if (user.getUserId() != 1) {
            throw new CustomException("无权限");
        }
        userPermissionsMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updatePermission(List<UserPermissions> userPermissions) {
        //先删除该用户所有的权限
//        Long userId = BaseContext.getCurrentId();
        //TODO:
        Long userId = 10L;
        userPermissionsMapper.deleteByUser(userId);
        //再添加所选权限
        this.addBatchPermission(userPermissions);
    }

}
