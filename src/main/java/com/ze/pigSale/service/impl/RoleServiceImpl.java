package com.ze.pigSale.service.impl;

import com.ze.pigSale.entity.Permissions;
import com.ze.pigSale.entity.Role;
import com.ze.pigSale.mapper.RoleMapper;
import com.ze.pigSale.service.PermissionService;
import com.ze.pigSale.service.RoleService;
import com.ze.pigSale.vo.RoleVo;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: ze
 * @Date: 2023-04-12-16:27
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionService permissionService;

    @Override
    public List<Role> getRoles() {
        return roleMapper.getRoles();
    }

    @Override
    public List<RoleVo> getRolesWithPermissions(List<Role> roles) {
        return roles.stream().map(item -> {
            Integer roleId = item.getRoleId();
            List<Permissions> list = permissionService.getByRoleId(roleId);
            RoleVo roleVo = new RoleVo();
            BeanUtils.copyProperties(item, roleVo);
            roleVo.setRolePermissions(list);
            return roleVo;
        }).collect(Collectors.toList());
    }
}
