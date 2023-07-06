package com.ze.pigSale.controller;

import com.ze.pigSale.common.Result;
import com.ze.pigSale.service.OrderDetailService;
import com.ze.pigSale.vo.OrderDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: ze
 * @Date: 2023-04-19-13:26
 */
@RestController
@Slf4j
@RequestMapping("/order/detail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 获取用户未评价订单
     * @param userId
     * @return
     */
    @GetMapping("/review/{userId}")
    public Result<List<OrderDetailVo>> listWithNoReview(@PathVariable("userId") Long userId){
        List<OrderDetailVo> orderDetailVos = orderDetailService.getByNoReview(userId);
        return Result.success(orderDetailVos);
    }
}
