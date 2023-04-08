package com.ze.pigSale.service;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.entity.Review;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-08-14:03
 */
public interface ReviewService {

    /**
     * 添加
     * @param review
     */
    void addReview(Review review);

    /**
     * 获取产品评论或用户评论
     * @param review
     * @return
     */
    PageInfo<Review> getReviewList(Integer currentPage, Integer pageSize, Review review,Integer queryWay);

    /**
     * 修改评论
     * @param review
     */
    void updateReview(Review review);

    /**
     * 删除评论
     * @param reviewId
     */
    void deleteReview(Long reviewId);
}
