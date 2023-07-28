package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Permissions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【permissions】的数据库操作Mapper
* @createDate 2023-04-09 16:07:38
* @Entity com.ze.pigSale.entity.Permissions
*/
@Mapper
public interface PermissionsMapper {
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
    Permissions getById(@Param("id") Integer id);

    /**
     * 根据role获取权限
     * @param RoleId
     * @return
     */
    List<Permissions> getByRoleId(@Param(("roleId")) Integer RoleId);
}




