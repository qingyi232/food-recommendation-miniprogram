package com.campus.food.service;

import com.campus.food.entity.Dish;
import com.campus.food.entity.Shop;

import java.util.List;

public interface RecommendService {
    List<Shop> recommendShops(Long userId, int limit);
    List<Dish> recommendDishes(Long userId, int limit);
    void recordBehavior(Long userId, String behaviorType, String targetType, Long targetId);
    List<Shop> getPopularShops(int limit);
    List<Dish> getPopularDishes(int limit);
}
