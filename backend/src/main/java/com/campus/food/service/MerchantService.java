package com.campus.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.food.entity.Merchant;

import java.util.Map;

public interface MerchantService extends IService<Merchant> {
    Map<String, Object> login(String username, String password);
    Merchant register(Merchant merchant);
    Merchant getMerchantInfo(Long merchantId);
    Merchant updateMerchantInfo(Merchant merchant);
    IPage<Merchant> getMerchantPage(int page, int size, String keyword, Integer status);
    void updateMerchantStatus(Long merchantId, Integer status);
    void addViolation(Long merchantId);
}
