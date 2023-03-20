package com.ze.pigSale.service.impl;

import com.ze.pigSale.entity.Product;
import com.ze.pigSale.mapper.CategoryProductMapper;
import com.ze.pigSale.mapper.ProductMapper;
import com.ze.pigSale.service.ProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-15-20:11
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryProductMapper categoryProductMapper;

    @Override
    public Product getProductById(Long id) {
        return null;
    }

    @Override
    public List<Product> getProductList(String productName, Long categoryId) {
        return productMapper.getAllProduct(productName, categoryId);
    }

    @Override
    public List<Product> getAllProductByName(String name) {
        return productMapper.getAllProductByName(name);
    }

    @Override
//    @Transactional
    public void insertProduct(Product product) {
        productMapper.insertProduct(product);
//        categoryProductMapper.insert(product);
    }

    @Override
    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        productMapper.deleteProduct(productId);
    }
}
