package com.ze.pigSale.service.impl;

import com.ze.pigSale.entity.Permissions;
import com.ze.pigSale.mapper.PermissionsMapper;
import com.ze.pigSale.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.zip.Inflater;

/**
 * @author: ze
 * @Date: 2023-04-12-15:10
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionsMapper permissionsMapper;

    @Override
    public List<Permissions> getList() {
        return permissionsMapper.getList();
    }

    @Override
    public Permissions getById(Long id) {
        return permissionsMapper.getById(id);
    }

    @Override
    public List<Permissions> getByRoleId(Integer RoleId) {
        return permissionsMapper.getByRoleId(RoleId);
    }
}
