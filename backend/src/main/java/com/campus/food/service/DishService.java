package com.campus.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.food.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    IPage<Dish> getDishPage(int page, int size, Long shopId, Long categoryId, String keyword);
    Dish getDishDetail(Long dishId);
    Dish addDish(Dish dish);
    Dish updateDish(Dish dish);
    void updateDishStatus(Long dishId, Integer status);
    List<Dish> getShopDishes(Long shopId);
    List<Dish> getDishByIds(List<Long> ids);
}
