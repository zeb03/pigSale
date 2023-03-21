package com.ze.pigSale.controller;

import com.ze.pigSale.common.Result;
import com.ze.pigSale.entity.Category;
import com.ze.pigSale.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * author: zebii
 * Date: 2023-03-15-23:04
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> list() {
        List<Category> categoryList = categoryService.getCategoryList();
        return Result.success(categoryList);
    }


    @PostMapping
    public Result<Category> save(@RequestBody Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        if (category.getParentId() == null) {
            category.setParentId(1L);
        }
        categoryService.insertCategory(category);
        return Result.success(category);
    }

    @PutMapping
    public Result<Category> edit(@RequestBody Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryService.updateCategory(category);
        return Result.success(category);
    }

    @DeleteMapping("/{categoryId}")
    public Result<String> delete(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return Result.success("删除成功");
    }
}
