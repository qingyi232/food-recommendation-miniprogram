package com.campus.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.food.entity.*;
import com.campus.food.mapper.*;
import com.campus.food.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteShopMapper favoriteShopMapper;
    @Autowired
    private FavoriteFoodMapper favoriteFoodMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private ShareRecordMapper shareRecordMapper;

    @Override
    public void favoriteShop(Long userId, Long shopId) {
        FavoriteShop existing = favoriteShopMapper.selectOne(
                new LambdaQueryWrapper<FavoriteShop>()
                        .eq(FavoriteShop::getUserId, userId)
                        .eq(FavoriteShop::getShopId, shopId));
        if (existing == null) {
            FavoriteShop favorite = new FavoriteShop();
            favorite.setUserId(userId);
            favorite.setShopId(shopId);
            favoriteShopMapper.insert(favorite);
        }
    }

    @Override
    public void unfavoriteShop(Long userId, Long shopId) {
        favoriteShopMapper.delete(new LambdaQueryWrapper<FavoriteShop>()
                .eq(FavoriteShop::getUserId, userId)
                .eq(FavoriteShop::getShopId, shopId));
    }

    @Override
    public boolean isFavoriteShop(Long userId, Long shopId) {
        return favoriteShopMapper.selectCount(new LambdaQueryWrapper<FavoriteShop>()
                .eq(FavoriteShop::getUserId, userId)
                .eq(FavoriteShop::getShopId, shopId)) > 0;
    }

    @Override
    public List<Shop> getFavoriteShops(Long userId) {
        List<FavoriteShop> favorites = favoriteShopMapper.selectList(
                new LambdaQueryWrapper<FavoriteShop>().eq(FavoriteShop::getUserId, userId));
        if (favorites.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> shopIds = favorites.stream().map(FavoriteShop::getShopId).collect(Collectors.toList());
        return shopMapper.selectBatchIds(shopIds);
    }

    @Override
    public void favoriteFood(Long userId, Long dishId) {
        FavoriteFood existing = favoriteFoodMapper.selectOne(
                new LambdaQueryWrapper<FavoriteFood>()
                        .eq(FavoriteFood::getUserId, userId)
                        .eq(FavoriteFood::getDishId, dishId));
        if (existing == null) {
            FavoriteFood favorite = new FavoriteFood();
            favorite.setUserId(userId);
            favorite.setDishId(dishId);
            favoriteFoodMapper.insert(favorite);
        }
    }

    @Override
    public void unfavoriteFood(Long userId, Long dishId) {
        favoriteFoodMapper.delete(new LambdaQueryWrapper<FavoriteFood>()
                .eq(FavoriteFood::getUserId, userId)
                .eq(FavoriteFood::getDishId, dishId));
    }

    @Override
    public boolean isFavoriteFood(Long userId, Long dishId) {
        return favoriteFoodMapper.selectCount(new LambdaQueryWrapper<FavoriteFood>()
                .eq(FavoriteFood::getUserId, userId)
                .eq(FavoriteFood::getDishId, dishId)) > 0;
    }

    @Override
    public List<Dish> getFavoriteFoods(Long userId) {
        List<FavoriteFood> favorites = favoriteFoodMapper.selectList(
                new LambdaQueryWrapper<FavoriteFood>().eq(FavoriteFood::getUserId, userId));
        if (favorites.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> dishIds = favorites.stream().map(FavoriteFood::getDishId).collect(Collectors.toList());
        return dishMapper.selectBatchIds(dishIds);
    }

    @Override
    public void shareFood(Long userId, Long targetId, String targetType, String shareType) {
        ShareRecord record = new ShareRecord();
        record.setUserId(userId);
        record.setTargetId(targetId);
        record.setTargetType(targetType);
        record.setShareType(shareType);
        shareRecordMapper.insert(record);
    }
}
