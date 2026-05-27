package com.campus.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.food.entity.OrderInfo;

import java.util.Map;

public interface OrderService extends IService<OrderInfo> {
    OrderInfo createOrder(OrderInfo order);
    IPage<OrderInfo> getOrderPage(int page, int size, Long userId, Long merchantId, Integer status);
    OrderInfo getOrderDetail(Long orderId);
    void updateOrderStatus(Long orderId, Integer status);
    Map<String, Object> getMerchantOrderStats(Long merchantId);
    Map<String, Object> getMerchantChartData(Long merchantId);
}
