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
     * @param review
     * @return
     */
    List<Review> getReviewListByUser(Review review);

    /**
     * 获取商品评论
     * @param review
     * @return
     */
    List<Review> getReviewListByProduct(Review review);

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




