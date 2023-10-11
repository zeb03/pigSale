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

    /**
     * 删除用户评价
     * @param userId
     */
    void deleteByUser(@Param("userId") Long userId);
}
