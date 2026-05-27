package com.campus.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.food.entity.OrderInfo;
import com.campus.food.entity.OrderItem;
import com.campus.food.mapper.OrderInfoMapper;
import com.campus.food.mapper.OrderItemMapper;
import com.campus.food.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campus.food.entity.Category;
import com.campus.food.entity.Dish;
import com.campus.food.entity.Shop;
import com.campus.food.mapper.CategoryMapper;
import com.campus.food.mapper.DishMapper;
import com.campus.food.mapper.ShopMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public OrderInfo createOrder(OrderInfo order) {
        order.setOrderNo(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        order.setStatus(0);
        this.save(order);

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrderId(order.getId());
                item.setAmount(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                orderItemMapper.insert(item);
            }
        }
        return order;
    }

    @Override
    public IPage<OrderInfo> getOrderPage(int page, int size, Long userId, Long merchantId, Integer status) {
        IPage<OrderInfo> orderPage = orderInfoMapper.selectOrderPage(
                new Page<>(page, size), userId, merchantId, status);
        for (OrderInfo order : orderPage.getRecords()) {
            List<OrderItem> items = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
            order.setItems(items);
        }
        return orderPage;
    }

    @Override
    public OrderInfo getOrderDetail(Long orderId) {
        OrderInfo order = this.getById(orderId);
        if (order != null) {
            List<OrderItem> items = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
            order.setItems(items);
        }
        return order;
    }

    @Override
    public void updateOrderStatus(Long orderId, Integer status) {
        OrderInfo order = new OrderInfo();
        order.setId(orderId);
        order.setStatus(status);
        this.updateById(order);
    }

    @Override
    public Map<String, Object> getMerchantOrderStats(Long merchantId) {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalOrders", orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getMerchantId, merchantId)));
        stats.put("pendingOrders", orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getMerchantId, merchantId).eq(OrderInfo::getStatus, 0)));
        stats.put("completedOrders", orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getMerchantId, merchantId).eq(OrderInfo::getStatus, 2)));

        List<OrderInfo> completedOrders = orderInfoMapper.selectList(
                new LambdaQueryWrapper<OrderInfo>()
                        .eq(OrderInfo::getMerchantId, merchantId)
                        .eq(OrderInfo::getStatus, 2));
        BigDecimal totalAmount = completedOrders.stream()
                .map(OrderInfo::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalAmount", totalAmount);

        LocalDate today = LocalDate.now();
        Date todayStart = java.sql.Timestamp.valueOf(LocalDateTime.of(today, LocalTime.MIN));
        Date todayEnd = java.sql.Timestamp.valueOf(LocalDateTime.of(today, LocalTime.MAX));

        stats.put("todayOrders", orderInfoMapper.selectCount(
                new LambdaQueryWrapper<OrderInfo>()
                        .eq(OrderInfo::getMerchantId, merchantId)
                        .ge(OrderInfo::getCreateTime, todayStart)
                        .le(OrderInfo::getCreateTime, todayEnd)));

        List<OrderInfo> todayCompleted = orderInfoMapper.selectList(
                new LambdaQueryWrapper<OrderInfo>()
                        .eq(OrderInfo::getMerchantId, merchantId)
                        .eq(OrderInfo::getStatus, 2)
                        .ge(OrderInfo::getCreateTime, todayStart)
                        .le(OrderInfo::getCreateTime, todayEnd));
        BigDecimal todayAmount = todayCompleted.stream()
                .map(OrderInfo::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("todayAmount", todayAmount);

        LocalDate monthStart = today.withDayOfMonth(1);
        Date monthStartDate = java.sql.Timestamp.valueOf(LocalDateTime.of(monthStart, LocalTime.MIN));
        List<OrderInfo> monthCompleted = orderInfoMapper.selectList(
                new LambdaQueryWrapper<OrderInfo>()
                        .eq(OrderInfo::getMerchantId, merchantId)
                        .eq(OrderInfo::getStatus, 2)
                        .ge(OrderInfo::getCreateTime, monthStartDate)
                        .le(OrderInfo::getCreateTime, todayEnd));
        BigDecimal monthAmount = monthCompleted.stream()
                .map(OrderInfo::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("monthAmount", monthAmount);

        return stats;
    }

    @Override
    public Map<String, Object> getMerchantChartData(Long merchantId) {
        Map<String, Object> result = new HashMap<>();

        // 1. 菜品销量TOP10（柱状图数据）
        List<Shop> shops = shopMapper.selectList(
                new LambdaQueryWrapper<Shop>().eq(Shop::getMerchantId, merchantId));
        List<Long> shopIds = new ArrayList<>();
        for (Shop shop : shops) {
            shopIds.add(shop.getId());
        }

        List<Map<String, Object>> dishSalesTop10 = new ArrayList<>();
        if (!shopIds.isEmpty()) {
            List<Dish> dishes = dishMapper.selectList(
                    new LambdaQueryWrapper<Dish>()
                            .in(Dish::getShopId, shopIds)
                            .eq(Dish::getStatus, 1)
                            .orderByDesc(Dish::getSales)
                            .last("LIMIT 10"));
            for (Dish dish : dishes) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", dish.getName());
                item.put("sales", dish.getSales() != null ? dish.getSales() : 0);
                dishSalesTop10.add(item);
            }
        }
        result.put("dishSalesTop10", dishSalesTop10);

        // 2. 各分类菜品销量分布（饼图数据）
        List<Map<String, Object>> categorySales = new ArrayList<>();
        if (!shopIds.isEmpty()) {
            List<Dish> allDishes = dishMapper.selectList(
                    new LambdaQueryWrapper<Dish>()
                            .in(Dish::getShopId, shopIds)
                            .eq(Dish::getStatus, 1));
            Map<Long, Integer> categoryMap = new HashMap<>();
            for (Dish dish : allDishes) {
                Long catId = dish.getCategoryId();
                if (catId != null) {
                    categoryMap.put(catId, categoryMap.getOrDefault(catId, 0)
                            + (dish.getSales() != null ? dish.getSales() : 0));
                }
            }
            List<Category> categories = categoryMapper.selectList(null);
            Map<Long, String> catNameMap = new HashMap<>();
            for (Category cat : categories) {
                catNameMap.put(cat.getId(), cat.getName());
            }
            for (Map.Entry<Long, Integer> entry : categoryMap.entrySet()) {
                if (entry.getValue() > 0) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", catNameMap.getOrDefault(entry.getKey(), "其他"));
                    item.put("value", entry.getValue());
                    categorySales.add(item);
                }
            }
            categorySales.sort((a, b) -> Integer.compare(
                    ((Number) b.get("value")).intValue(),
                    ((Number) a.get("value")).intValue()));
        }
        result.put("categorySales", categorySales);

        return result;
    }
}
