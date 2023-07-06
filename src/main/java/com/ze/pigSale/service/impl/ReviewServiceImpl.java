package com.ze.pigSale.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.entity.Review;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.mapper.ReviewMapper;
import com.ze.pigSale.service.OrdersService;
import com.ze.pigSale.service.ReviewService;
import com.ze.pigSale.service.UserService;
import com.ze.pigSale.vo.ReviewVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author: zebii
 * Date: 2023-04-08-14:05
 */
@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private OrdersService ordersService;

    @Override
    public void addReview(Review review) {

        review.setPublishTime(LocalDateTime.now());
        Long userId = BaseContext.getCurrentId();
        review.setUserId(userId);
        log.info("添加评论信息：{}", review);
        reviewMapper.add(review);
    }

    @Override
    public PageInfo<ReviewVo> getReviewPage(Integer currentPage, Integer pageSize, Review review, Integer queryWay) {

        PageMethod.startPage(currentPage, pageSize);
        List<Review> reviews = null;
        if (queryWay == 1) {
            reviews = reviewMapper.getReviewListByUser(review.getUserId());
        } else {
            reviews = reviewMapper.getReviewListByProduct(review.getProductId());
        }

        PageInfo<Review> reviewPageInfo = new PageInfo<>(reviews);
        PageInfo<ReviewVo> pageInfo = new PageInfo<>();

        List<ReviewVo> reviewVos = reviews.stream().map(item -> {
            ReviewVo reviewVo = new ReviewVo();
            Long userId = item.getUserId();
            User user = userService.getUserById(userId);
            if (item.getAnonymous() == 1) {
                item.setUsername("匿名用户");
            } else {
                item.setUsername(user.getUsername());
            }
            BeanUtils.copyProperties(item, reviewVo);
            reviewVo.setAvatar(user.getImage());
            return reviewVo;
        }).collect(Collectors.toList());

        BeanUtils.copyProperties(reviewPageInfo, pageInfo);
        pageInfo.setList(reviewVos);

        log.info("返回评论信息：{}", pageInfo);
        return pageInfo;
    }

    @Override
    public List<Review> getListByUser(Long userId) {
        return reviewMapper.getReviewListByUser(userId);
    }

    @Override
    public List<Review> getListByProduct(Long productId) {
        return reviewMapper.getReviewListByProduct(productId);
    }

    @Override
    public void updateReview(Review review) {
        reviewMapper.updateById(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewMapper.deleteById(reviewId);
    }

    @Override
    public void deleteByUser(Long userId) {

    }

}
