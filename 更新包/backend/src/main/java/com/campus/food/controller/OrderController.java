package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.entity.OrderInfo;
import com.campus.food.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<?> createOrder(@RequestBody OrderInfo order, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        order.setUserId(userId);
        return Result.success(orderService.createOrder(order));
    }

    @GetMapping("/user/page")
    public Result<?> getUserOrders(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) Integer status,
                                   HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(orderService.getOrderPage(page, size, userId, null, status));
    }

    @GetMapping("/merchant/page")
    public Result<?> getMerchantOrders(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(required = false) Integer status,
                                       HttpServletRequest request) {
        Long merchantId = (Long) request.getAttribute("userId");
        return Result.success(orderService.getOrderPage(page, size, null, merchantId, status));
    }

    @GetMapping("/detail/{id}")
    public Result<?> getOrderDetail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    @PutMapping("/status/{id}")
    public Result<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        orderService.updateOrderStatus(id, params.get("status"));
        return Result.success();
    }

    @GetMapping("/merchant/stats")
    public Result<?> getMerchantOrderStats(HttpServletRequest request) {
        Long merchantId = (Long) request.getAttribute("userId");
        return Result.success(orderService.getMerchantOrderStats(merchantId));
    }

    @GetMapping("/merchant/chart-data")
    public Result<?> getMerchantChartData(HttpServletRequest request) {
        Long merchantId = (Long) request.getAttribute("userId");
        return Result.success(orderService.getMerchantChartData(merchantId));
    }
}
