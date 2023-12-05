package com.ze.pigSale.service.impl;

import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.dto.OrderDetailDto;
import com.ze.pigSale.entity.*;
import com.ze.pigSale.enums.PermissionEnum;
import com.ze.pigSale.mapper.OrderDetailMapper;
import com.ze.pigSale.mapper.ProductMapper;
import com.ze.pigSale.service.*;
import com.ze.pigSale.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * author: zebii
 * Date: 2023-03-15-20:11
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private UserPermissionService userPermissionService;

    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Override
    public Product getProductById(Long id) {
        return productMapper.getProductById(id);
    }

    @Override
    public List<Product> getProductList(String productName, Long categoryId) {
        return productMapper.getAllProduct(productName, categoryId);
    }

    @Override
    public List<Product> getAllProductByName(String name) {
        return productMapper.getAllProductByName(name);
    }

    @Override
    public void insertProduct(Product product) {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.ADD_PRODUCT);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }

        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productMapper.insertProduct(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(Product product) {
        //判断权限
//        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.EDIT_PRODUCT);
//        if (!hasPermission) {
//            throw new CustomException(CommonUtil.NOT_PERMISSION);
//        }

        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateProduct(product);

        //若购物车存在该商品，则也要进行修改
        Cart cart = cartService.getCart(product.getProductId());
        if (cart != null) {
            String productName = product.getProductName();
            BigDecimal price = product.getPrice();
            String image = product.getImage();
            cart.setAmount(price);
            cart.setName(productName);
            cart.setImage(image);
            cartService.updateCartById(cart);
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.DELETE_PRODUCT);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }
        productMapper.deleteProduct(productId);
    }

    @Override
    public Map<String, Integer> getSalesRank(Integer month) {
        //判断权限
//        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.VIEW_DATA);
//        if (!hasPermission) {
//            throw new CustomException(CommonUtil.NOT_PERMISSION);
//        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now.minusMonths(month);

        log.info("now:" + now);
        log.info("time:" + time);

        List<OrderDetailDto> list = orderDetailMapper.getListByTime(now, time);

        HashMap<String, Integer> rankMap = new LinkedHashMap<>();

        list.stream().map(item -> {

            Long productId = item.getProductId();

            Integer quantity = item.getQuantity();

            Product product = productMapper.getProductById(productId);

            if (product == null) {
                return null;
            }

            String productName = product.getProductName();

            rankMap.put(productName, rankMap.getOrDefault(productName, 0) + quantity);

            return null;
        }).collect(Collectors.toList());

        return rankMap;
    }

    @Override
    public Map<String, BigDecimal> getAllBenefit(Integer month) {

        //判断权限
//        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.VIEW_DATA);
//        if (!hasPermission) {
//            throw new CustomException(CommonUtil.NOT_PERMISSION);
//        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = now.minusMonths(month);

        log.info("now:" + now);
        log.info("time:" + time);

        List<OrderDetailDto> list = orderDetailMapper.getListByTime(now, time);

        HashMap<String, BigDecimal> benefitMap = new LinkedHashMap<>();

        list.stream().map(item -> {

            Long productId = item.getProductId();

            Integer quantity = item.getQuantity();

            Product product = productMapper.getProductById(productId);

            String productName = product.getProductName();

            benefitMap.put(productName, benefitMap.getOrDefault(productName, BigDecimal.ZERO)
                    .add(product.getPrice().multiply(new BigDecimal(quantity))));

            return null;
        }).collect(Collectors.toList());

        return benefitMap;
    }

    @Override
    public List<BigDecimal> getBenefit() {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.VIEW_DATA);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }

        // 获取当前年份
        int currentYear = YearMonth.now().getYear();

        // 构建收益数据列表
        List<BigDecimal> benefitData = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            // 构建当前年份和月份对应的YearMonth对象
            YearMonth yearMonth = YearMonth.of(currentYear, month);

            // 根据年份和月份获取起始时间和结束时间
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            // 根据YearMonth查询对应的订单详情列表
            List<OrderDetailDto> orders = orderDetailMapper.getListByTime(endDateTime, startDateTime);

            // 计算月份收益
            BigDecimal monthlyTotal = calculateMonthlyBenefit(orders);
            benefitData.add(monthlyTotal);
        }

        return benefitData;
    }

    @Override
    public List<Integer> getOrderCount() {
        //判断权限
        boolean hasPermission = userPermissionService.hasPermission(PermissionEnum.VIEW_DATA);
        if (!hasPermission) {
            throw new CustomException(CommonUtil.NOT_PERMISSION);
        }

        // 获取当前年份
        int currentYear = YearMonth.now().getYear();

        // 构建收益数据列表
        List<Integer> orderCount = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            // 构建当前年份和月份对应的YearMonth对象
            YearMonth yearMonth = YearMonth.of(currentYear, month);

            // 根据年份和月份获取起始时间和结束时间
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            // 根据YearMonth查询对应的订单详情列表
            Integer count = ordersService.getCountByTime(startDateTime, endDateTime);
            orderCount.add(count);
        }

        return orderCount;
    }


    // 根据订单详情计算月收益
    private BigDecimal calculateMonthlyBenefit(List<OrderDetailDto> orderDetails) {
        BigDecimal totalBenefit = BigDecimal.ZERO;

        for (OrderDetailDto orderDetail : orderDetails) {
            BigDecimal price = orderDetail.getPrice();
            int quantity = orderDetail.getQuantity();

            BigDecimal benefit = price.multiply(BigDecimal.valueOf(quantity));
            totalBenefit = totalBenefit.add(benefit);
        }

        return totalBenefit;
    }


}
