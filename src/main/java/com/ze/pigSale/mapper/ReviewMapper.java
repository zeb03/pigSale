package com.ze.pigSale.mapper;

import com.ze.pigSale.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author z
* @description 针对表【review】的数据库操作Mapper
* @createDate 2023-04-08 13:56:36
* @Entity com.ze.pigSale.entity.Review
*/
@Mapper
public interface ReviewMapper {

    /**
     * 添加
     * @param review
     */
    void add(Review review);

    /**
     * 获取用户评论
     * @param userId
     * @return
     */
    List<Review> getReviewListByUser(@Param("userId") Long userId);

    /**
     * 获取商品评论
     * @param productId
     * @return
     */
    List<Review> getReviewListByProduct(@Param("productId") Long productId);

    /**
     * 根据id修改评论
     * @param review
     */
    void updateById(Review review);

    /**
     * 删除评论
     * @param reviewId
     */
    void deleteById(@Param("reviewId") Long reviewId);
}




