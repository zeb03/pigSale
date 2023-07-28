package com.ze.pigSale.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName role
 */
@Data
public class Role implements Serializable {

    private Integer roleId;

    private String roleName;

    private String description;

    private static final long serialVersionUID = 1L;
}