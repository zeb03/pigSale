package com.ze.pigSale.controller;

import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Cart;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.service.CartService;
import com.ze.pigSale.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-22-10:37
 */
@RestController
@Slf4j
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;

//    @PostMapping("/add")
//    public Result<Cart> add(@RequestBody Cart cart) {
//        log.info("addCart:{}", cart);
//
//        //根据用户id，产品id判断该产品是否已经存在
//        Cart userCart = cartService.getCart(cart.getProductId());
//
//        //若存在，则数量加1，并修改
//        if (userCart != null) {
//            Integer quantity = userCart.getQuantity();
//            userCart.setQuantity(quantity + 1);
//            cartService.updateCartById(userCart);
//        } else {
//            //否则加入购物车
//            cart.setCreateTime(LocalDateTime.now());
//            cartService.addCart(cart);
//            userCart = cart;
//        }
//
//        return Result.success(userCart);
//    }

    @GetMapping("/list")
    public Result<List<Cart>> list() {
        //获取用户id
        Long currentId = BaseContext.getCurrentId();
        //根据id获取购物车
        List<Cart> cartList = cartService.getCartListByUser(currentId);
        return Result.success(cartList);
    }


    @DeleteMapping("/clean")
    public Result<String> clean() {
        Long userId = BaseContext.getCurrentId();
        cartService.cleanCart(userId);
        return Result.success("清空购物车成功");
    }

    @PutMapping("/edit")
    public Result<String> edit(@RequestBody Cart cart) {
        //获取商品
        Cart userCart = cartService.getCart(cart.getProductId());
        log.info("cart:{}", cart);

        //加入购物车
        if (userCart == null) {
            //设置购物车信息
            Product product = productService.getProductById(cart.getProductId());
            cart.setQuantity(cart.getQuantity());
            cart.setImage(product.getImage());
            cart.setAmount(product.getPrice());
            cart.setName(product.getProductName());
            cart.setCreateTime(LocalDateTime.now());
            cartService.addCart(cart);
            return Result.success("添加成功");
        }

        //数量增减
        Integer quantity = cart.getQuantity();
        userCart.setQuantity(quantity);

        if (quantity == 0) {
            //移除
            cartService.deleteCart(userCart);
        } else {
            cartService.updateCartById(userCart);
        }

        return Result.success("修改成功");
    }

}
