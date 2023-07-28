package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.User;
import com.ze.pigSale.entity.UserPermissions;
import com.ze.pigSale.service.UserPermissionService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【user_permissions】的数据库操作Mapper
* @createDate 2023-04-09 16:06:56
* @Entity com.ze.pigSale.entity.UserPermissionService
*/
@Mapper
public interface UserPermissionsMapper {
    /**
     * 通过id获取
     * @param id
     * @return
     */
    UserPermissions getById(@Param("id") Long id);

    /**
     * 通过userId获取权限
     * @param user
     * @return
     */
    List<UserPermissions> getByUserId(User user);

    /**
     * 赋予管理员权限
     * @param userPermissions
     */
    void save(UserPermissions userPermissions);

    /**
     * 删除用户权限
     * @param id
     *
     */
    void deleteById(@Param("id") Long id);

    /**
     * 删除用户所有权限
     * @param userId
     */
    void deleteByUser(@Param("userId") Long userId);
}




