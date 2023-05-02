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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author: zebii
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
        BigDecimal total = new BigDecimal(0);
        for (Review review : reviews) {
            total = total.add(review.getRating());
        }
        double totalRating = total.doubleValue();
        double rating = totalRating / reviews.size();
        String format = new DecimalFormat("#.0").format(rating);
        productVo.setRating(Double.parseDouble(format));
        return Result.success(productVo);
    }

    /**
     * 分页查询所有产品
     *
     * @param currentPage
     * @param pageSize
     * @param keyword
     * @param categoryId
     * @return
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
     * @return
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
     * @return
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
     * @return
     */
    @DeleteMapping("/{productId}")
    public Result<String> delete(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return Result.success("删除成功");
    }
}
