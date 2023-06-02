package com.ze.pigSale.service.impl;

import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.controller.CommonController;
import com.ze.pigSale.entity.*;
import com.ze.pigSale.enums.PermissionEnum;
import com.ze.pigSale.mapper.OrderDetailMapper;
import com.ze.pigSale.mapper.ProductMapper;
import com.ze.pigSale.service.CartService;
import com.ze.pigSale.service.ProductService;
import com.ze.pigSale.service.UserPermissionService;
import com.ze.pigSale.service.UserService;
import com.ze.pigSale.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private UserService userService;

    @Autowired
    private UserPermissionService userPermissionService;

    @Autowired
    private OrderDetailMapper orderDetailMapper;


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
    public void insertProduct(Product product) {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.ADD_PRODUCT);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }

        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productMapper.insertProduct(product);
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.EDIT_PRODUCT);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }

        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateProduct(product);

        //若购物车存在该商品，则也要进行修改
        Cart cart = cartService.getCart(product.getProductId());
        if (cart != null) {
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
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.DELETE_PRODUCT);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }
        productMapper.deleteProduct(productId);
    }

    @Override
    public Map<String, Integer> getSalesRank() {
        List<OrderDetail> list = orderDetailMapper.getList();
        HashMap<String, Integer> rankMap = new LinkedHashMap<>();

        list.stream().map(item -> {
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();
            Product product = productMapper.getProductById(productId);
            String productName = product.getProductName();
            rankMap.put(productName, rankMap.getOrDefault(productName, 0) + quantity);
            return null;
        }).collect(Collectors.toList());

        return rankMap;
    }
}
