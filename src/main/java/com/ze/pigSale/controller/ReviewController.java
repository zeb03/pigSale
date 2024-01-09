package com.ze.pigSale.controller;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Orders;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.entity.Review;
import com.ze.pigSale.service.OrdersService;
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

    @Autowired
    private OrdersService ordersService;

    /**
     * 发布评论
     * @param review
     * @return
     */
    @PostMapping("/publish")
    public Result<String> publish(@RequestBody Review review) {
        log.info("review:" + review);
        if ("".equals(review.getImage())) {
            review.setImage(null);
        }
        reviewService.addReview(review);
        return Result.success("评论发布成功");
    }

    /**
     * 查询所有评论
     * @param currentPage
     * @param pageSize
     * @param review
     * @param queryWay
     * @return
     */
    @GetMapping("/page")
    public Result<PageInfo<ReviewVo>> page(Integer currentPage, Integer pageSize, Review review, Integer queryWay) {
        PageInfo<ReviewVo> reviewPageInfo = reviewService.getReviewPage(currentPage, pageSize, review, queryWay);
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

    /**
     * 编辑评论
     * @param review
     * @return
     */
    @PutMapping("/edit")
    public Result<String> edit(@RequestBody Review review) {
        reviewService.updateReview(review);
        return Result.success("修改评论成功");
    }

    /**
     * 删除评论
     * @param reviewId
     * @return
     */
    @DeleteMapping("/remove/{reviewId}")
    public Result<String> remove(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return Result.success("删除评论成功");
    }

}
