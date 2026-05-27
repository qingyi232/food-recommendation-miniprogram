package com.campus.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.food.entity.Shop;

import java.util.List;

public interface ShopService extends IService<Shop> {
    IPage<Shop> getShopPage(int page, int size, Long categoryId, String keyword, Long merchantId);
    Shop getShopDetail(Long shopId);
    Shop addShop(Shop shop);
    Shop updateShop(Shop shop);
    void updateShopStatus(Long shopId, Integer status);
    List<Shop> getMerchantShops(Long merchantId);
    List<Shop> getShopsByIds(List<Long> ids);
}
