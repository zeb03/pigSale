package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author z
* @description 针对表【product】的数据库操作Mapper
* @createDate 2023-03-15 19:52:59
* @Entity com.ze.pigSale.entity.Product
*/
@Mapper
public interface ProductMapper {

    /**
     * 根据id查找产品
     * @param productId
     * @return
     */
    Product getProductById(@Param("productId") Long productId);

    /* 查询所有产品 */
    List<Product> getAllProduct(@Param("productName") String productName, @Param("categoryId") Long categoryId);

    /* 根据名字查询所有产品*/
    List<Product> getAllProductByName(@Param("name") String name);

    /**
     * 添加商品
     * @param product
     */
    void insertProduct(Product product);

    /**
     * 修改商品
     * @param product
     */
    void updateProduct(Product product);

    /**
     * 删除商品
     * @param productId
     */
    void deleteProduct(@Param("productId") Long productId);
}




