package com.ze.pigSale.controller;

import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Cart;
import lombok.extern.slf4j.Slf4j;
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

//    @PostMapping("/add")
//    public Result<Cart> add(@RequestBody Cart cart) {
//        log.info("cart: {}", cart);
//
//        ShoppingCart cart = shoppingCartService.get(shoppingCart);
//
//        if (cart != null) {
//            //该项已经存在，数量加一
//            Integer number = cart.getNumber();
//            cart.setNumber(number + 1);
//            shoppingCartService.updateById(cart);
//        } else {
//            //添加该项
//            shoppingCart.setNumber(1);
//            shoppingCart.setCreateTime(LocalDateTime.now());
//            shoppingCartService.save(shoppingCart);
//            cart = shoppingCart;
//        }
//        return R.success(cart);
//    }
//
//    /**
//     * 查看购物车
//     *
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<ShoppingCart>> list() {
//        //获取用户id
//        Long userId = BaseContext.getCurrentId();
//        List<ShoppingCart> list = shoppingCartService.listByUser(userId);
//        return R.success(list);
//    }
//
//
//    /**
//     * 清空购物车
//     *
//     * @return
//     */
//    @DeleteMapping("/clean")
//    public R<String> clean() {
//        Long userId = BaseContext.getCurrentId();
//        shoppingCartService.clean(userId);
//        return R.success("成功清空购物车");
//    }
//
//    /**
//     * 减少商品数量
//     * @param shoppingCart
//     * @return
//     */
//    @PostMapping("/sub")
//    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
//
//        //获取商品
//        ShoppingCart cart = shoppingCartService.get(shoppingCart);
//
//        Integer number = cart.getNumber();
//        if (number == 1) {
//            //数量为1则进行删除
//            shoppingCartService.removeById(cart.getId());
//        } else {
//            cart.setNumber(number - 1);
//            shoppingCartService.updateById(cart);
//        }
//
//        return R.success(cart);
//    }
}
