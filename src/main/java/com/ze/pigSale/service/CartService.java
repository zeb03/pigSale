package com.ze.pigSale.service;

import com.ze.pigSale.entity.Cart;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-31-16:39
 */

public interface CartService {

    /**
     * 根据用户id，产品id获取购物车
     * @param productId
     * @return
     */
    Cart getCart(Long productId);

    /**
     * 根据用户id获取购物车
     * @param userId
     * @return
     */
    List<Cart> getCartListByUser(Long userId);

    /**
     * 根据id获取对应的购物车项
     * @param cartItemIds
     * @return
     */
    List<Cart> getCartListByIds(List<Long> cartItemIds);

    /**
     * 根据id修改
     * @param cart
     */
    void updateCartById(Cart cart);

    /**
     * 添加购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 清空购物车
     * @param userId
     */
    void cleanCart(Long userId);

    /**
     *
     * @param cart
     */
    void deleteCart(Cart cart);
}
