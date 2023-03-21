package com.ze.pigSale.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.ze.pigSale.common.Result;
import com.ze.pigSale.dto.ProductDto;
import com.ze.pigSale.entity.Category;
import com.ze.pigSale.entity.Product;
import com.ze.pigSale.entity.User;
import com.ze.pigSale.service.CategoryService;
import com.ze.pigSale.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author: zebii
 * Date: 2023-03-15-19:53
 */
@Slf4j
@RestController()
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;


    /**
     * 获取产品及种类
     *
     * @param productDto
     * @return
     */
    @GetMapping
    public Result<ProductDto> getOne(ProductDto productDto) {
        log.info("ProductDto：{}", productDto);
        //获取产品
        Product product = productService.getProductById(productDto.getProductId());
        //获取种类
        Category category = categoryService.getCategoryById(productDto.getCategoryId());
        if (category != null) {
            productDto.setCategoryName(category.getCategoryName());
        }
        if (product != null) {
            BeanUtils.copyProperties(product, productDto);
        }
        log.info("ProductDto：{}", productDto);
        return Result.success(productDto);
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
    public Result<PageInfo<ProductDto>> page(Integer currentPage, Integer pageSize, String keyword, Long categoryId) {
        PageMethod.startPage(currentPage, pageSize);
        List<Product> productList = productService.getProductList(keyword, categoryId);

        PageInfo<Product> sourcePageInfo = new PageInfo<>(productList);
        PageInfo<ProductDto> targetPageInfo = new PageInfo<>();

        BeanUtils.copyProperties(sourcePageInfo, targetPageInfo);

        List<ProductDto> productDtoList = productList.stream().map((item -> {
            //创建dto
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(item, productDto);

            //获取种类名
            Long id = productDto.getCategoryId();
            Category category = categoryService.getCategoryById(id);

            if (category != null) {
                String name = category.getCategoryName();
                productDto.setCategoryName(name);
            }

            return productDto;
        })).collect(Collectors.toList());

        targetPageInfo.setList(productDtoList);
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
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
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
        product.setUpdateTime(LocalDateTime.now());
        productService.updateProduct(product);
        return Result.success(product);
    }

    @DeleteMapping("/{productId}")
    public Result<String> delete(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return Result.success("删除成功");
    }
}
