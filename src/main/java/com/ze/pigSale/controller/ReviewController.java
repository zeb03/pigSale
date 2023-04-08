package com.ze.pigSale.controller;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Review;
import com.ze.pigSale.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * author: zebii
 * Date: 2023-04-08-13:59
 */
@Slf4j
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/publish")
    public Result<String> publish(@RequestBody Review review){
        reviewService.addReview(review);
        return Result.success("评论发布成功");
    }

    @GetMapping("/list")
    public Result<PageInfo<Review>> page(Integer currentPage, Integer pageSize, Review review, Integer queryWay){
        PageInfo<Review> reviewPageInfo = reviewService.getReviewList(currentPage, pageSize, review,queryWay);
        return Result.success(reviewPageInfo);
    }

    @PutMapping("/edit")
    public Result<String> edit(@RequestBody Review review){
        reviewService.updateReview(review);
        return Result.success("修改评论成功");
    }

    @DeleteMapping("/remove/{reviewId}")
    public Result<String> remove(@PathVariable("reviewId") Long reviewId){
        reviewService.deleteReview(reviewId);
        return Result.success("删除评论成功");
    }
}
