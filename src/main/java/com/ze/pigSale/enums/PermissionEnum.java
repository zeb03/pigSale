package com.ze.pigSale.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author: ze
 * @Date: 2023-05-02-21:06
 */
@Getter
public enum PermissionEnum {
    //添加商品
    ADD_PRODUCT(1,"添加商品"),
    //修改商品
    EDIT_PRODUCT(2,"编辑商品"),
    //删除商品
    DELETE_PRODUCT(3,"删除商品"),
    VIEW_ORDER(4,"查看订单"),
    EDIT_ORDER(5,"编辑订单"),
    CANCEL_ORDER(6,"取消订单"),
    VIEW_USER(7,"查看用户"),
    EDIT_USER(8,"编辑用户"),
    EDIT_ADMIN(9,"编辑管理员"),
    ADD_ADMIN(10,"添加管理员"),
    VIEW_DATA(11,"查看数据"),
    HANDLE_FEEDBACK(12,"查看数据");

    /**
     * 权限id
     */
    @EnumValue
    private Integer permissionId;

    /**
     * 权限名称
     */
    @JsonValue
    private String permissionName;

    PermissionEnum(Integer id, String name){
        this.permissionId = id;
        this.permissionName = name;
    }
}
