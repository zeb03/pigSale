package com.ze.pigSale.controller;


import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.common.CustomException;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Review;
import com.ze.pigSale.service.ReviewService;
import com.ze.pigSale.vo.ProductVo;
import com.ze.pigSale.entity.Category;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.service.CategoryService;
import com.ze.pigSale.service.ProductService;
import com.ze.pigSale.vo.SalesVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: zebii
 * Date: 2023-03-15-19:53
 */
@Slf4j
@RestController()
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    private CategoryService categoryService;

    private ReviewService reviewService;


    /**
     * 获取某商品
     * @param productVo
     * @return
     */
    @GetMapping
    public Result<ProductVo> getOne(ProductVo productVo) {
        log.info("ProductVo：{}", productVo);
        //获取产品
        Product product = productService.getProductById(productVo.getProductId());
        //获取种类
        Category category = categoryService.getCategoryById(productVo.getCategoryId());
        if (category != null) {
            productVo.setCategoryName(category.getCategoryName());
        }
        if (product != null) {
            BeanUtils.copyProperties(product, productVo);
        }
        log.info("ProductVo：{}", productVo);
        return Result.success(productVo);
    }

    /**
     * 根据id查看商品详情
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public Result<ProductVo> detail(@PathVariable("productId") Long productId) {
        //获取商品
        Product product = productService.getProductById(productId);

        if (product == null) {
            throw new CustomException("无法获取商品，商品id错误");
        }

        //创建dto并复制属性
        ProductVo productVo = new ProductVo();
        BeanUtils.copyProperties(product, productVo);

        //根据类别id获取名称并赋值
        Category category = categoryService.getCategoryById(productVo.getCategoryId());
        if (category != null) {
            productVo.setCategoryName(category.getCategoryName());
        }

        //获取商品评分
        List<Review> reviews = reviewService.getListByProduct(productId);
        if (reviews == null || reviews.isEmpty()) {
            productVo.setRating(0.0);
        } else {
            BigDecimal total = new BigDecimal(0);
            for (Review review : reviews) {
                total = total.add(review.getRating());
            }
            double totalRating = total.doubleValue();
            double rating = totalRating / reviews.size();
            String format = new DecimalFormat("#.0").format(rating);
            productVo.setRating(Double.parseDouble(format));
        }
        log.info("rating: " + productVo.getRating());
        return Result.success(productVo);
    }

    /**
     * 分页查询所有产品
     *
     * @param currentPage
     * @param pageSize
     * @param keyword
     * @param categoryId
     */
    @GetMapping("/page")
    public Result<PageInfo<ProductVo>> page(Integer currentPage, Integer pageSize, String keyword, Long categoryId) {
        PageMethod.startPage(currentPage, pageSize);
        List<Product> productList = productService.getProductList(keyword, categoryId);

        PageInfo<Product> sourcePageInfo = new PageInfo<>(productList);
        PageInfo<ProductVo> targetPageInfo = new PageInfo<>();

        BeanUtils.copyProperties(sourcePageInfo, targetPageInfo);

        List<ProductVo> productVoList = productList.stream().map((item -> {
            //创建dto
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(item, productVo);

            //获取种类名
            Long id = productVo.getCategoryId();
            Category category = categoryService.getCategoryById(id);

            if (category != null) {
                String name = category.getCategoryName();
                productVo.setCategoryName(name);
            }

            return productVo;
        })).collect(Collectors.toList());

        targetPageInfo.setList(productVoList);
        return Result.success(targetPageInfo);
    }

    /**
     * 新增产品
     *
     * @param product
     */
    @PostMapping
    public Result<Product> add(@RequestBody Product product) {
        log.info("addProduct: {}", product);

        productService.insertProduct(product);
        return Result.success(product);
    }

    /**
     * 修改产品
     *
     * @param product
     */
    @PutMapping
    public Result<Product> edit(@RequestBody Product product) {
        log.info("updateProduct: {}", product);

        productService.updateProduct(product);
        return Result.success(product);
    }

    /**
     * 移除商品
     *
     * @param productId
     */
    @DeleteMapping("/{productId}")
    public Result<String> delete(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return Result.success("删除成功");
    }

    /**
     * 获取最近销量
     *
     */
    @GetMapping("/salesRank")
    public Result<SalesVO> getSalesRank(Integer month) {
        log.info("getSalesRank");

        Map<String, Integer> salesRank = productService.getSalesRank(month);

        Set<String> keySet = salesRank.keySet();
        Collection<Integer> values = salesRank.values();

        SalesVO salesVO = new SalesVO();
        salesVO.setProductNames(keySet);
        salesVO.setSales(values);

        log.info("data:" + salesVO);
        return Result.success(salesVO);
    }

    /**
     * 获取最近收益
     *
     */
    @GetMapping("/benefit/all")
    public Result<Map<String, BigDecimal>> getAllBenefit(Integer month) {
        log.info("getBenefit");
        Map<String, BigDecimal> benefit = productService.getAllBenefit(month);
        return Result.success(benefit);
    }

    /**
     * 获取最近一年总收益
     *
     */
    @GetMapping("/benefit")
    public Result<List<BigDecimal>> getBenefit() {
        log.info("getBenefit");
        List<BigDecimal> benefit = productService.getBenefit();
        return Result.success(benefit);
    }

    /**
     * 获取指定时间内订单成交量
     */
    @GetMapping("/thisYearOrders")
    public Result<List<Integer>> getOrderCount() {

        log.info("getOrderCount");
        List<Integer> orderCount = productService.getOrderCount();
        return Result.success(orderCount);
    }
}
