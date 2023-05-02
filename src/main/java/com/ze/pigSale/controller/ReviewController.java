package com.ze.pigSale.controller;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.entity.Review;
import com.ze.pigSale.service.ProductService;
import com.ze.pigSale.service.ReviewService;
import com.ze.pigSale.vo.ReviewVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ProductService productService;

    @PostMapping("/publish")
    public Result<String> publish(@RequestBody Review review) {
        if ("".equals(review.getImage())) {
            review.setImage(null);
        }
        reviewService.addReview(review);
        return Result.success("评论发布成功");
    }

    @GetMapping("/page")
    public Result<PageInfo<Review>> page(Integer currentPage, Integer pageSize, Review review, Integer queryWay) {
        log.info("商品评论：{}", currentPage);
        PageInfo<Review> reviewPageInfo = reviewService.getReviewPage(currentPage, pageSize, review, queryWay);
        return Result.success(reviewPageInfo);
    }

    /**
     * 获取评论及其商品信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/list/{userId}")
    public Result<List<ReviewVo>> list(@PathVariable("userId") Long userId) {
        //获取用户评论
        // TODO:
        userId = 10L;
        List<Review> reviews = reviewService.getListByUser(userId);

        //根据productId获取商品信息
        List<ReviewVo> reviewVos = reviews.stream().map(item -> {
            ReviewVo reviewVo = new ReviewVo();
            BeanUtils.copyProperties(item, reviewVo);
            Long productId = reviewVo.getProductId();
            Product product = productService.getProductById(productId);
            log.info("productId:" + productId);
            log.info("product:" + product);
            if (product != null) {
                reviewVo.setProductImage(product.getImage());
                reviewVo.setDescription(product.getDescription());
            }
            return reviewVo;
        }).collect(Collectors.toList());

        return Result.success(reviewVos);
    }

    @PutMapping("/edit")
    public Result<String> edit(@RequestBody Review review) {
        reviewService.updateReview(review);
        return Result.success("修改评论成功");
    }

    @DeleteMapping("/remove/{reviewId}")
    public Result<String> remove(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return Result.success("删除评论成功");
    }
}
