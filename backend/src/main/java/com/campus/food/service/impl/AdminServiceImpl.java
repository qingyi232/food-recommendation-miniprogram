package com.campus.food.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.food.entity.*;
import com.campus.food.mapper.*;
import com.campus.food.service.AdminService;
import com.campus.food.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Map<String, Object> login(String username, String password) {
        String md5Password = DigestUtil.md5Hex(password);
        Admin admin = this.getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, username)
                .eq(Admin::getPassword, md5Password));
        if (admin == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(admin.getId(), admin.getUsername(), "admin");
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        admin.setPassword(null);
        result.put("adminInfo", admin);
        return result;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userMapper.selectCount(null));
        stats.put("merchantCount", merchantMapper.selectCount(null));
        stats.put("shopCount", shopMapper.selectCount(null));
        stats.put("dishCount", dishMapper.selectCount(null));
        stats.put("orderCount", orderInfoMapper.selectCount(null));
        stats.put("reviewCount", reviewMapper.selectCount(null));
        stats.put("pendingMerchants", merchantMapper.selectCount(
                new LambdaQueryWrapper<Merchant>().eq(Merchant::getStatus, 0)));
        stats.put("violationReviews", reviewMapper.selectCount(
                new LambdaQueryWrapper<Review>().eq(Review::getStatus, 2)));
        return stats;
    }

    @Override
    public Map<String, Object> getVisualizationData() {
        Map<String, Object> data = new HashMap<>();

        // 各分类店铺数量统计
        List<Category> categories = categoryMapper.selectList(null);
        List<Map<String, Object>> categoryStats = new ArrayList<>();
        for (Category category : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", category.getName());
            item.put("value", shopMapper.selectCount(
                    new LambdaQueryWrapper<Shop>().eq(Shop::getCategoryId, category.getId())));
            categoryStats.add(item);
        }
        data.put("categoryShopStats", categoryStats);

        // 各分类菜品数量统计
        List<Map<String, Object>> categoryDishStats = new ArrayList<>();
        for (Category category : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", category.getName());
            item.put("value", dishMapper.selectCount(
                    new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, category.getId())));
            categoryDishStats.add(item);
        }
        data.put("categoryDishStats", categoryDishStats);

        // 店铺评分Top10
        List<Shop> topShops = shopMapper.selectList(
                new LambdaQueryWrapper<Shop>().orderByDesc(Shop::getRating).last("LIMIT 10"));
        List<Map<String, Object>> shopRatingTop = new ArrayList<>();
        for (Shop shop : topShops) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", shop.getName());
            item.put("rating", shop.getRating());
            item.put("sales", shop.getTotalSales());
            shopRatingTop.add(item);
        }
        data.put("shopRatingTop", shopRatingTop);

        // 菜品销量Top10
        List<Dish> topDishes = dishMapper.selectList(
                new LambdaQueryWrapper<Dish>().orderByDesc(Dish::getSales).last("LIMIT 10"));
        List<Map<String, Object>> dishSalesTop = new ArrayList<>();
        for (Dish dish : topDishes) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", dish.getName());
            item.put("sales", dish.getSales());
            item.put("price", dish.getPrice());
            dishSalesTop.add(item);
        }
        data.put("dishSalesTop", dishSalesTop);

        // 订单状态分布
        Map<String, Object> orderStatusStats = new HashMap<>();
        orderStatusStats.put("pending", orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getStatus, 0)));
        orderStatusStats.put("confirmed", orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getStatus, 1)));
        orderStatusStats.put("completed", orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getStatus, 2)));
        orderStatusStats.put("cancelled", orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getStatus, 3)));
        data.put("orderStatusStats", orderStatusStats);

        // 用户性别分布
        Map<String, Object> genderStats = new HashMap<>();
        genderStats.put("unknown", userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getGender, 0)));
        genderStats.put("male", userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getGender, 1)));
        genderStats.put("female", userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getGender, 2)));
        data.put("genderStats", genderStats);

        // 商家状态分布
        Map<String, Object> merchantStatusStats = new HashMap<>();
        merchantStatusStats.put("pending", merchantMapper.selectCount(
                new LambdaQueryWrapper<Merchant>().eq(Merchant::getStatus, 0)));
        merchantStatusStats.put("normal", merchantMapper.selectCount(
                new LambdaQueryWrapper<Merchant>().eq(Merchant::getStatus, 1)));
        merchantStatusStats.put("disabled", merchantMapper.selectCount(
                new LambdaQueryWrapper<Merchant>().eq(Merchant::getStatus, 2)));
        data.put("merchantStatusStats", merchantStatusStats);

        // 评价评分分布
        List<Map<String, Object>> reviewRatingDist = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("rating", i + "星");
            item.put("count", reviewMapper.selectCount(
                    new LambdaQueryWrapper<Review>().eq(Review::getRating, i)));
            reviewRatingDist.add(item);
        }
        data.put("reviewRatingDist", reviewRatingDist);

        return data;
    }
}
