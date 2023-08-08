package com.ze.pigSale.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.entity.Cart;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.mapper.CartMapper;
import com.ze.pigSale.mapper.ProductMapper;
import com.ze.pigSale.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-31-16:40
 */
@Service
@Slf4j
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public Cart getCart(Long productId) {
        Cart cart = new Cart();
        Long userId = BaseContext.getCurrentId();
        log.info("userId:{}", userId);
        cart.setUserId(userId);
        cart.setProductId(productId);
        return cartMapper.getCart(cart);
    }

    @Override
    public List<Cart> getCartListByUser(Long userId) {
        return cartMapper.listByUserId(userId);
    }

    @Override
    public List<Cart> getCartListByIds(List<Long> cartItemIds) {
        return cartMapper.ListByIds(cartItemIds);
    }

    @Override
    public void updateCartById(Cart cart) {
        cartMapper.updateCartById(cart);
    }

    @Override
    public void addCart(Cart cart) {
        Long userId = BaseContext.getCurrentId();
        cart.setUserId(userId);
        // temp
//        cart.setUserId(7L);
        cartMapper.save(cart);
    }

    @Override
    public void cleanCart(Long userId) {
        cartMapper.deleteByUserId(userId);
    }

    @Override
    public void deleteCart(Cart cart) {
        cartMapper.deleteById(cart);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        cartMapper.deleteBatch(ids);
    }
}
