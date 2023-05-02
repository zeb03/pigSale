package com.ze.pigSale.controller;

import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Permissions;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;
import com.ze.pigSale.service.PermissionService;
import com.ze.pigSale.service.UserPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Date: 2023-04-09-17:10
 *
 * @author ze
 */
@RestController
@Slf4j
@RequestMapping("/permissions")
public class PermissionsController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/list")
    public Result<List<Permissions>> list() {
        List<Permissions> list = permissionService.getList();
        log.info("所有权限:" + list);
        return Result.success(list);
    }

}
