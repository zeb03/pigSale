package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【category】的数据库操作Mapper
* @createDate 2023-03-15 19:52:59
* @Entity com.ze.pigSale.entity.Category
*/
@Mapper
public interface CategoryMapper {
    /* 根据id获取category*/
    Category getCategoryById(@Param("id") Long id);

    /**
     * 获取所有的种类
     * @return
     */
    List<Category> getCategoryList();

    /**
     * 插入种类
     */
    void insertCategory(Category category);
}




