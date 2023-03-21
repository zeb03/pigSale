package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-03-13 15:34:09
* @Entity com.ze.pig.entity.User
*/
@Mapper
public interface UserMapper {

    /**
     * 通过id查找用户
     * @param id
     * @return
     */
    User getUserById(@Param("id") Long id);

    /**
     * 通过name查找用户
     * @param username
     * @return
     */
    User getUserByName(@Param("name") String username);

    /**
     * 通过role查找用户
     * @param role
     * @return
     */
    List<User> getUserByRole(@Param("role") Integer role);

    /**
     * 查询所有用户
     * @return
     */
    List<User> getUserList();


    /**
     * 保存用户
     * @param user
     */
    void insertUser(User user);

    /**
     * 通过phone或username查找用户
     * @param phone
     * @param username
     * @return
     */
    User getUserByPhoneOrName(@Param("phone") String phone, @Param("username") String username);

    /**
     * 修改用户
     * @param user
     * @return
     */
    void updateUser(User user);

    /**
     * 删除用户
     * @param userId
     */
    void deleteUser(@Param("userId") Long userId);

}




