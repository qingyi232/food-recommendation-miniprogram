package com.campus.food.service;

import com.campus.food.entity.Dish;
import com.campus.food.entity.Shop;

import java.util.List;

public interface FavoriteService {
    void favoriteShop(Long userId, Long shopId);
    void unfavoriteShop(Long userId, Long shopId);
    boolean isFavoriteShop(Long userId, Long shopId);
    List<Shop> getFavoriteShops(Long userId);

    void favoriteFood(Long userId, Long dishId);
    void unfavoriteFood(Long userId, Long dishId);
    boolean isFavoriteFood(Long userId, Long dishId);
    List<Dish> getFavoriteFoods(Long userId);

    void shareFood(Long userId, Long targetId, String targetType, String shareType);
}
