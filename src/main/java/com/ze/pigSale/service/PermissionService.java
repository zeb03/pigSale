package com.ze.pigSale.service;

import com.ze.pigSale.entity.Permissions;

import java.util.List;

/**
 * @author: ze
 * @Date: 2023-04-12-15:09
 */
public interface PermissionService {
    /**
     * 查看所有权限
     * @return
     */
    List<Permissions> getList();

    /**
     * 根据id获取
     * @param id
     * @return
     */
    Permissions getById(Integer id);

    /**
     * 根据role获取权限
     * @param RoleId
     * @return
     */
    List<Permissions> getByRoleId(Integer RoleId);
}
