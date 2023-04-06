package com.ze.pigSale.controller;

import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.dto.OrdersDto;
import com.ze.pigSale.entity.Orders;
import com.ze.pigSale.service.OrdersService;
import com.ze.pigSale.service.impl.OrdersServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * author: zebii
 * Date: 2023-04-04-20:08
 */
@RestController
@Slf4j
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/page")
    public Result<PageInfo<OrdersDto>> page(int currentPage, int pageSize, Long ordersId, LocalDateTime beginTime, LocalDateTime endTime) {
        PageInfo<OrdersDto> pageInfo = ordersService.getPageWithDetail(currentPage, pageSize, ordersId, beginTime, endTime);
        return Result.success(pageInfo);
    }

//    @PostMapping("/submit")
//    public Result<String> submit(@RequestBody Orders orders){
//        log.info("订单数据：{}", orders);
//        ordersService.submit(orders);
//        return Result.success("下单成功");
//    }

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody OrdersDto ordersDto){
        log.info("订单数据：{}", ordersDto);
        ordersService.submit(ordersDto);
        return Result.success("下单成功");
    }

}
