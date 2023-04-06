package com.ze.pigSale.service.impl;

import com.ze.pigSale.entity.Cart;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.mapper.CategoryProductMapper;
import com.ze.pigSale.mapper.ProductMapper;
import com.ze.pigSale.service.CartService;
import com.ze.pigSale.service.ProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private CartService cartService;

    @Override
    public Product getProductById(Long id) {
        return productMapper.getProductById(id);
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
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateProduct(product);

        //若购物车存在该商品，则也要进行修改
        Cart cart = cartService.getCart(product.getProductId());
        if (cart != null){
            String productName = product.getProductName();
            BigDecimal price = product.getPrice();
            String image = product.getImage();
            cart.setAmount(price);
            cart.setName(productName);
            cart.setImage(image);
            cartService.updateCartById(cart);
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        productMapper.deleteProduct(productId);
    }
}
