package com.ze.pigSale.entity;

import java.io.Serializable;

import com.ze.pigSale.enums.PermissionEnum;
import lombok.Data;

/**
 * @author ze
 * @TableName permissions
 */
@Data
public class Permissions implements Serializable {
    private PermissionEnum permissionId;

    private String description;

    private static final long serialVersionUID = 1L;
}