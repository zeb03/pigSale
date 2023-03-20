package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * author: zebii
 * Date: 2023-03-18-16:03
 */
@Mapper
public interface CategoryProductMapper {
    /**
     * 插入数据
     *
     * @param product
     */
    void insert(Product product);
}
