package com.ze.pigSale.controller;


import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.dto.OrdersDTO;
import com.ze.pigSale.dto.TimeDTO;
import com.ze.pigSale.entity.Orders;
import com.ze.pigSale.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;


/**
 * author: zebii
 * Date: 2023-04-04-20:08
 */
@RestController
@Slf4j
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    /**
     * 管理员查询订单
     *
     * @param currentPage
     * @param pageSize
     * @param ordersId
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("admin/page")
    public Result<PageInfo<OrdersDTO>> page(Integer currentPage, Integer pageSize, Long ordersId, LocalDateTime beginTime, LocalDateTime endTime) {
        PageInfo<OrdersDTO> pageInfo = ordersService.getPageWithDetail(currentPage, pageSize, ordersId, beginTime, endTime);
        return Result.success(pageInfo);
    }

    /**
     * 用户提交订单
     *
     * @param ordersDto
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody OrdersDTO ordersDto) {
        log.info("订单数据：{}", ordersDto);
        ordersService.submit(ordersDto);
        return Result.success("下单成功");
    }

    /**
     * 用户查询订单
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/user/page")
    public Result<PageInfo<OrdersDTO>> page(int currentPage, int pageSize, TimeDTO timeDto) {
        log.info("{}", timeDto);
        PageInfo<OrdersDTO> ordersDtoPageInfo = ordersService.getListByUserId(currentPage, pageSize, timeDto.getBeginTime(), timeDto.getEndTime());
        return Result.success(ordersDtoPageInfo);
    }


    /**
     * 修改订单状态
     *
     * @param orders
     * @return
     */
    @PutMapping("/status")
    public Result<String> status(@RequestBody Orders orders) {
        log.info("修改订单状态:{}", orders);
        ordersService.updateStatus(orders);
        return Result.success("修改订单状态成功");
    }

    /**
     * 再来一单
     *
     * @param orders
     * @return
     */
    @PostMapping("/again")
    public Result<String> again(@RequestBody Orders orders) {
        ordersService.again(orders);
        return Result.success("提交成功");
    }


    /**
     * 取消订单或退款
     *
     * @param ordersId
     * @param request
     * @return
     */
    @DeleteMapping("/cancel/{ordersId}")
    public Result<String> cancel(@PathVariable("ordersId") Long ordersId, HttpServletRequest request) {
        return ordersService.cancelOrders(ordersId, request);
    }

    /**
     * 同意退款
     *
     * @param orders
     * @return
     */
    @PutMapping("/handle/agree")
    public Result<String> agree(@RequestBody Orders orders) {
        ordersService.agree(orders);
        return Result.success("已同意退款");
    }

    /**
     * 拒绝退款
     *
     * @param orders
     * @param request
     * @return
     */
    @PutMapping("/handle/disagree")
    public Result<String> disagree(@RequestBody Orders orders, HttpServletRequest request) {
        ordersService.disagree(orders, request);
        return Result.success("已拒绝退款");
    }
}
