package com.ze.pigSale.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.ze.pigSale.enums.PermissionEnum;
import lombok.Data;

/**
 * @author ze
 * @TableName user_permissions
 */
@Data
public class UserPermissions implements Serializable {
    private Long id;

    private Long userId;

    private PermissionEnum permissionId;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private static final long serialVersionUID = 1L;
}