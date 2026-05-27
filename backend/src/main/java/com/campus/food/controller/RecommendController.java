package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @GetMapping("/shops")
    public Result<?> recommendShops(@RequestParam(defaultValue = "10") int limit,
                                    HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(recommendService.recommendShops(userId, limit));
    }

    @GetMapping("/dishes")
    public Result<?> recommendDishes(@RequestParam(defaultValue = "10") int limit,
                                     HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(recommendService.recommendDishes(userId, limit));
    }

    @PostMapping("/behavior")
    public Result<?> recordBehavior(@RequestBody Map<String, String> params,
                                    HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String behaviorType = params.get("behaviorType");
        String targetType = params.get("targetType");
        Long targetId = Long.valueOf(params.get("targetId"));
        recommendService.recordBehavior(userId, behaviorType, targetType, targetId);
        return Result.success();
    }

    @GetMapping("/popular/shops")
    public Result<?> getPopularShops(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(recommendService.getPopularShops(limit));
    }

    @GetMapping("/popular/dishes")
    public Result<?> getPopularDishes(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(recommendService.getPopularDishes(limit));
    }
}
