package com.ze.pigSale.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ze.pigSale.common.BaseContext;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.dto.OrdersDto;
import com.ze.pigSale.entity.Orders;
import com.ze.pigSale.service.OrdersService;
import com.ze.pigSale.service.impl.OrdersServiceImpl;
import com.ze.pigSale.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Result<PageInfo<OrdersDto>> page(int currentPage, int pageSize, Long ordersId, LocalDateTime beginTime, LocalDateTime endTime) {
        PageInfo<OrdersDto> pageInfo = ordersService.getPageWithDetail(currentPage, pageSize, ordersId, beginTime, endTime);
        return Result.success(pageInfo);
    }

    /**
     * 用户提交订单
     *
     * @param ordersDto
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody OrdersDto ordersDto) {
        log.info("订单数据：{}", ordersDto);
        ordersService.submit(ordersDto);
        return Result.success("下单成功");
    }

    /**
     * 用户查询订单
     *
     * @param currentPage
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("user/page")
    public Result<PageInfo<OrdersDto>> list(int currentPage, int pageSize, LocalDateTime beginTime, LocalDateTime endTime) {
        PageInfo<OrdersDto> ordersDtoPageInfo = ordersService.getListByUserId(currentPage, pageSize, beginTime, endTime);
        return Result.success(ordersDtoPageInfo);
    }

    /**
     * 修改订单状态
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
     * @param ordersId
     * @param request
     * @return
     */
    @PutMapping("/cancel/{ordersId}")
    public Result<String> cancel(@PathVariable("ordersId") Long ordersId, HttpServletRequest request) {

        //获取此订单
        Orders oneOrders = ordersService.getById(ordersId);

        //判断订单状态
        if (oneOrders == null) {
            throw new CustomException("订单id错误");
        }

        Integer status = oneOrders.getStatus();
        if (status == 6) {
            throw new CustomException("该订单已取消");
        }

        if (status == 1) {
            //设置订单状态
            oneOrders.setStatus(6);
        } else {
            request.getSession().setAttribute("userStatus", oneOrders.getStatus());
            oneOrders.setStatus(5);
        }

        ordersService.updateById(oneOrders);

        if (oneOrders.getStatus() == 6) {
            return Result.success("取消成功");
        }

        return Result.success("等待管理员同意");
    }

    /**
     * 同意退款
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
