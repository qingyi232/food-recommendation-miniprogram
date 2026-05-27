package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.entity.Dish;
import com.campus.food.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    public Result<?> getDishList(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) Long shopId,
                                 @RequestParam(required = false) Long categoryId,
                                 @RequestParam(required = false) String keyword) {
        return Result.success(dishService.getDishPage(page, size, shopId, categoryId, keyword));
    }

    @GetMapping("/detail/{id}")
    public Result<?> getDishDetail(@PathVariable Long id) {
        return Result.success(dishService.getDishDetail(id));
    }

    @PostMapping("/add")
    public Result<?> addDish(@RequestBody Dish dish) {
        return Result.success(dishService.addDish(dish));
    }

    @PutMapping("/update")
    public Result<?> updateDish(@RequestBody Dish dish) {
        return Result.success(dishService.updateDish(dish));
    }

    @PutMapping("/status/{id}")
    public Result<?> updateDishStatus(@PathVariable Long id, @RequestParam Integer status) {
        dishService.updateDishStatus(id, status);
        return Result.success();
    }

    @GetMapping("/shop/{shopId}")
    public Result<?> getShopDishes(@PathVariable Long shopId) {
        return Result.success(dishService.getShopDishes(shopId));
    }
}
