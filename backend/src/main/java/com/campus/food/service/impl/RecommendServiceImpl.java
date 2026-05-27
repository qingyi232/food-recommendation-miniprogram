package com.campus.food.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.food.entity.*;
import com.campus.food.mapper.*;
import com.campus.food.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private DishMapper dishMapper;

    private static final Map<String, BigDecimal> BEHAVIOR_WEIGHTS = new HashMap<String, BigDecimal>() {{
        put("view", new BigDecimal("1.0"));
        put("click", new BigDecimal("2.0"));
        put("favorite", new BigDecimal("3.0"));
        put("share", new BigDecimal("3.0"));
        put("order", new BigDecimal("5.0"));
        put("review", new BigDecimal("4.0"));
    }};

    @Override
    public List<Shop> recommendShops(Long userId, int limit) {
        Set<Long> recommendedIds = new LinkedHashSet<>();

        // 1. 基于用户行为的协同过滤推荐
        List<Long> collaborativeIds = getCollaborativeRecommendations(userId, "shop", limit);
        recommendedIds.addAll(collaborativeIds);

        // 2. 基于用户口味偏好的内容推荐
        List<Long> contentBasedIds = getContentBasedShopRecommendations(userId, limit);
        recommendedIds.addAll(contentBasedIds);

        // 3. 基于用户历史行为的推荐
        List<Map<String, Object>> preferences = userBehaviorMapper.selectUserPreferences(userId, "shop", limit);
        for (Map<String, Object> pref : preferences) {
            recommendedIds.add(Long.valueOf(pref.get("target_id").toString()));
        }

        // 4. 不足时用热门店铺补充
        if (recommendedIds.size() < limit) {
            List<Shop> popular = getPopularShops(limit - recommendedIds.size());
            for (Shop shop : popular) {
                recommendedIds.add(shop.getId());
            }
        }

        List<Long> finalIds = new ArrayList<>(recommendedIds);
        if (finalIds.size() > limit) {
            finalIds = finalIds.subList(0, limit);
        }

        if (finalIds.isEmpty()) {
            return getPopularShops(limit);
        }
        return shopMapper.selectBatchIds(finalIds);
    }

    @Override
    public List<Dish> recommendDishes(Long userId, int limit) {
        Set<Long> recommendedIds = new LinkedHashSet<>();

        // 1. 协同过滤推荐
        List<Long> collaborativeIds = getCollaborativeRecommendations(userId, "dish", limit);
        recommendedIds.addAll(collaborativeIds);

        // 2. 基于口味偏好的内容推荐
        List<Long> contentBasedIds = getContentBasedDishRecommendations(userId, limit);
        recommendedIds.addAll(contentBasedIds);

        // 3. 基于行为记录
        List<Map<String, Object>> preferences = userBehaviorMapper.selectUserPreferences(userId, "dish", limit);
        for (Map<String, Object> pref : preferences) {
            recommendedIds.add(Long.valueOf(pref.get("target_id").toString()));
        }

        // 4. 热门菜品补充
        if (recommendedIds.size() < limit) {
            List<Dish> popular = getPopularDishes(limit - recommendedIds.size());
            for (Dish dish : popular) {
                recommendedIds.add(dish.getId());
            }
        }

        List<Long> finalIds = new ArrayList<>(recommendedIds);
        if (finalIds.size() > limit) {
            finalIds = finalIds.subList(0, limit);
        }

        if (finalIds.isEmpty()) {
            return getPopularDishes(limit);
        }
        return dishMapper.selectBatchIds(finalIds);
    }

    @Override
    public void recordBehavior(Long userId, String behaviorType, String targetType, Long targetId) {
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setBehaviorType(behaviorType);
        behavior.setTargetType(targetType);
        behavior.setTargetId(targetId);
        behavior.setScore(BEHAVIOR_WEIGHTS.getOrDefault(behaviorType, new BigDecimal("1.0")));
        userBehaviorMapper.insert(behavior);
    }

    @Override
    public List<Shop> getPopularShops(int limit) {
        return shopMapper.selectList(new LambdaQueryWrapper<Shop>()
                .eq(Shop::getStatus, 1)
                .orderByDesc(Shop::getRating)
                .orderByDesc(Shop::getTotalSales)
                .last("LIMIT " + limit));
    }

    @Override
    public List<Dish> getPopularDishes(int limit) {
        return dishMapper.selectList(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getStatus, 1)
                .orderByDesc(Dish::getSales)
                .orderByDesc(Dish::getRating)
                .last("LIMIT " + limit));
    }

    private List<Long> getCollaborativeRecommendations(Long userId, String targetType, int limit) {
        // 找到相似用户
        List<Map<String, Object>> similarUsers = userBehaviorMapper.selectSimilarUsers(userId, 10);
        if (similarUsers.isEmpty()) {
            return new ArrayList<>();
        }

        String userIds = similarUsers.stream()
                .map(u -> u.get("user_id").toString())
                .collect(Collectors.joining(","));

        // 获取相似用户喜欢但当前用户未接触的项目
        List<Map<String, Object>> items = userBehaviorMapper.selectCollaborativeItems(
                userIds, targetType, userId, limit);

        return items.stream()
                .map(item -> Long.valueOf(item.get("target_id").toString()))
                .collect(Collectors.toList());
    }

    private List<Long> getContentBasedShopRecommendations(Long userId, int limit) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getTastePreference() == null) {
            return new ArrayList<>();
        }

        try {
            JSONObject preference = JSON.parseObject(user.getTastePreference());
            List<Integer> categoryIds = preference.getJSONArray("categories") != null
                    ? preference.getJSONArray("categories").toJavaList(Integer.class)
                    : new ArrayList<>();

            if (categoryIds.isEmpty()) {
                return new ArrayList<>();
            }

            List<Shop> shops = shopMapper.selectList(new LambdaQueryWrapper<Shop>()
                    .in(Shop::getCategoryId, categoryIds)
                    .eq(Shop::getStatus, 1)
                    .orderByDesc(Shop::getRating)
                    .last("LIMIT " + limit));

            return shops.stream().map(Shop::getId).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<Long> getContentBasedDishRecommendations(Long userId, int limit) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getTastePreference() == null) {
            return new ArrayList<>();
        }

        try {
            JSONObject preference = JSON.parseObject(user.getTastePreference());
            Integer spicyLevel = preference.getInteger("spicy");

            LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>()
                    .eq(Dish::getStatus, 1);

            if (spicyLevel != null && spicyLevel > 0) {
                wrapper.le(Dish::getSpicyLevel, spicyLevel);
            }

            List<Integer> categoryIds = preference.getJSONArray("categories") != null
                    ? preference.getJSONArray("categories").toJavaList(Integer.class)
                    : new ArrayList<>();

            if (!categoryIds.isEmpty()) {
                wrapper.in(Dish::getCategoryId, categoryIds);
            }

            wrapper.orderByDesc(Dish::getRating).last("LIMIT " + limit);
            List<Dish> dishes = dishMapper.selectList(wrapper);
            return dishes.stream().map(Dish::getId).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
