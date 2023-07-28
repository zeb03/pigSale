package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author z
* @description 针对表【role】的数据库操作Mapper
* @createDate 2023-04-09 00:07:39
* @Entity com.ze.pigSale.entity.Role
*/
@Mapper
public interface RoleMapper {
    /**
     * 获取所有管理员角色
     * @return
     */
    List<Role> getRoles();
}




