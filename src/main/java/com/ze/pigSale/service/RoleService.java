package com.ze.pigSale.service;

import com.ze.pigSale.entity.Permissions;
import com.ze.pigSale.entity.Role;
import com.ze.pigSale.vo.RoleVo;

import java.util.List;

/**
 * @author: ze
 * @Date: 2023-04-12-16:27
 */
public interface RoleService {
    /**
     * 获取所有管理员角色
     * @return
     */
    List<Role> getRoles();

    /**
     * 根据id获取权限
     * @param roles
     * @return
     */
    List<RoleVo> getRolesWithPermissions(List<Role> roles);
}
