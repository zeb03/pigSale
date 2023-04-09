package com.ze.pigSale.service.impl;

import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;
import com.ze.pigSale.mapper.UserPermissionsMapper;
import com.ze.pigSale.service.UserPermissionService;
import com.ze.pigSale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<UserPermissions> getByUserId() {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getUserById(userId);
        return userPermissionsMapper.getByUserId(user);
    }

    public boolean hasPermission(String operation){
        List<UserPermissions> permissions = this.getByUserId();
        boolean hasPermission = false;
        for (UserPermissions permission : permissions) {
            String permissionName = permission.getPermissionName();
            if (operation.equals(permissionName)){
                hasPermission = true;
            }
        }

        return hasPermission;
    }
}
