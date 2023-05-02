package com.ze.pigSale.service;

import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;

import java.util.List;

/**
 * Date: 2023-04-09-16:09
 * @author ze
 */
public interface UserPermissionService {
    /**
     * 获取用户权限
     * @param userId
     * @return
     */
    List<UserPermissions> getByUserId(Long userId);

    /**
     * 判断用户是否具有权限
     *
     * @param operation
     * @return
     */
    boolean hasPermission(String operation);

    /**
     * 赋予管理员权限
     * @param userPermissions
     */
    void addPermission(UserPermissions userPermissions);

    /**
     * 批量赋予管理员权限
     * @param userPermissions
     */
    void addBatchPermission(List<UserPermissions> userPermissions);

    /**
     * 根据id删除用户权限
     * @param id
     */
    void deletePermissionById(Long id);

    /**
     * 修改用户权限
     * @param userPermissions
     */
    void updatePermission(List<UserPermissions> userPermissions);
}
