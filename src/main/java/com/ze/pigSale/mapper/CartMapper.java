package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【cart】的数据库操作Mapper
* @createDate 2023-03-13 15:33:54
* @Entity com.ze.pig.entity.Cart
*/
@Mapper
public interface CartMapper {

    /**
     * 根据用户id，产品id获取购物车
     * @param cart
     * @return
     */
    Cart getCart(Cart cart);


    /**
     * 根据id获取对应的购物车项
     * @param cartItemIds
     * @return
     */
    List<Cart> ListByIds(@Param("cartItemIds") List<Long> cartItemIds);


    /**
     * 根据用户id获取购物车
     * @param userId
     * @return
     */
    List<Cart> listByUserId(@Param("userId") Long userId);

    /**
     * 根据id修改
     * @param cart
     */
    void updateById(Cart cart);

    /**
     * 保存购物车
     * @param cart
     */
    void save(Cart cart);

    /**
     * 清空购物车
     * @param userId
     */
    void deleteByUserId(@Param("userId") Long userId);

    /**
     *
     * @param cart
     */
    void deleteById(Cart cart);
}




