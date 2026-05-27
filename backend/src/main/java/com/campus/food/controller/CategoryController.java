package com.campus.food.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.food.common.Result;
import com.campus.food.entity.Category;
import com.campus.food.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("/list")
    public Result<?> getCategoryList() {
        return Result.success(categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getStatus, 1)
                        .orderByAsc(Category::getSortOrder)));
    }

    @PostMapping("/add")
    public Result<?> addCategory(@RequestBody Category category) {
        category.setStatus(1);
        categoryMapper.insert(category);
        return Result.success(category);
    }

    @PutMapping("/update")
    public Result<?> updateCategory(@RequestBody Category category) {
        categoryMapper.updateById(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteCategory(@PathVariable Long id) {
        categoryMapper.deleteById(id);
        return Result.success();
    }
}
