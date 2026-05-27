package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.entity.Shop;
import com.campus.food.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/list")
    public Result<?> getShopList(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) Long categoryId,
                                 @RequestParam(required = false) String keyword) {
        return Result.success(shopService.getShopPage(page, size, categoryId, keyword, null));
    }

    @GetMapping("/detail/{id}")
    public Result<?> getShopDetail(@PathVariable Long id) {
        return Result.success(shopService.getShopDetail(id));
    }

    @PostMapping("/add")
    public Result<?> addShop(@RequestBody Shop shop, HttpServletRequest request) {
        Long merchantId = (Long) request.getAttribute("userId");
        shop.setMerchantId(merchantId);
        return Result.success(shopService.addShop(shop));
    }

    @PutMapping("/update")
    public Result<?> updateShop(@RequestBody Shop shop) {
        return Result.success(shopService.updateShop(shop));
    }

    @PutMapping("/status/{id}")
    public Result<?> updateShopStatus(@PathVariable Long id, @RequestParam Integer status) {
        shopService.updateShopStatus(id, status);
        return Result.success();
    }

    @GetMapping("/merchant")
    public Result<?> getMerchantShops(HttpServletRequest request) {
        Long merchantId = (Long) request.getAttribute("userId");
        return Result.success(shopService.getMerchantShops(merchantId));
    }

    @GetMapping("/merchant/page")
    public Result<?> getMerchantShopPage(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        Long merchantId = (Long) request.getAttribute("userId");
        return Result.success(shopService.getShopPage(page, size, null, null, merchantId));
    }
}
