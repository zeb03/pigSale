package com.ze.pigSale.service;

import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-09-16:09
 */
public interface UserPermissionService {
    /**
     * @param user
     * @return
     */
    List<UserPermissions> getByUserId();

    /**
     * 判断用户是否具有权限
     *
     * @param operation
     * @return
     */
    boolean hasPermission(String operation);
}
