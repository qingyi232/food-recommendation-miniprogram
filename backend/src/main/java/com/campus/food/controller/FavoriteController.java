package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/shop")
    public Result<?> favoriteShop(@RequestBody Map<String, Long> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.favoriteShop(userId, params.get("shopId"));
        return Result.success();
    }

    @DeleteMapping("/shop/{shopId}")
    public Result<?> unfavoriteShop(@PathVariable Long shopId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.unfavoriteShop(userId, shopId);
        return Result.success();
    }

    @GetMapping("/shop/check/{shopId}")
    public Result<?> isFavoriteShop(@PathVariable Long shopId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(favoriteService.isFavoriteShop(userId, shopId));
    }

    @GetMapping("/shop/list")
    public Result<?> getFavoriteShops(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(favoriteService.getFavoriteShops(userId));
    }

    @PostMapping("/food")
    public Result<?> favoriteFood(@RequestBody Map<String, Long> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.favoriteFood(userId, params.get("dishId"));
        return Result.success();
    }

    @DeleteMapping("/food/{dishId}")
    public Result<?> unfavoriteFood(@PathVariable Long dishId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.unfavoriteFood(userId, dishId);
        return Result.success();
    }

    @GetMapping("/food/check/{dishId}")
    public Result<?> isFavoriteFood(@PathVariable Long dishId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(favoriteService.isFavoriteFood(userId, dishId));
    }

    @GetMapping("/food/list")
    public Result<?> getFavoriteFoods(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(favoriteService.getFavoriteFoods(userId));
    }

    @PostMapping("/share")
    public Result<?> share(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Long targetId = Long.valueOf(params.get("targetId"));
        String targetType = params.get("targetType");
        String shareType = params.get("shareType");
        favoriteService.shareFood(userId, targetId, targetType, shareType);
        return Result.success();
    }
}
