package com.ze.pigSale.service;

import com.ze.pigSale.entity.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * author: zebii
 * Date: 2023-03-15-20:10
 */
public interface ProductService {

    /**
     * 根据id查找产品
     * @param id
     * @return
     */
    Product getProductById(Long id);

    /**
     * 获取商品
     * @param productName
     * @param categoryId
     * @return
     */
    List<Product> getProductList(String productName, Long categoryId);

    /**
     * 根据名字查询商品
     * @param name
     * @return
     */
    List<Product> getAllProductByName(String name);

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
    void deleteProduct(Long productId);

    /**
     * 获取销量排行
     */
    Map<String, Integer> getSalesRank();
}
