/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ze.pigSale.service;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.entity.Review;
import com.ze.pigSale.vo.ReviewVo;

import java.util.List;

/**
 * Date: 2023-04-08-14:03
 * @author ze
 */
public interface ReviewService {

    /**
     * 添加
     * @param review
     */
    void addReview(Review review);

    /**
     * 获取产品评论或用户评论的分页信息
     * @param currentPage
     * @param pageSize
     * @param review
     * @param queryWay
     * @return
     */
    PageInfo<ReviewVo> getReviewPage(Integer currentPage, Integer pageSize, Review review, Integer queryWay);

    /**
     * 通过用户id获取评论列表
     * @param userId
     * @return
     */
    List<Review> getListByUser(Long userId);

    /**
     * 通过商品id获取评论列表
     * @param productId
     * @return
     */
    List<Review> getListByProduct(Long productId);

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

    /**
     * 删除用户评价
     * @param userId
     */
    void deleteByUser(Long userId);
}
