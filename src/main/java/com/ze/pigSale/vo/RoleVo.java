package com.ze.pigSale.vo;

import com.ze.pigSale.entity.Permissions;
import com.ze.pigSale.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * @author: ze
 * @Date: 2023-04-23-17:06
 */
@Data
public class RoleVo extends Role {
    private List<Permissions> rolePermissions;
}
