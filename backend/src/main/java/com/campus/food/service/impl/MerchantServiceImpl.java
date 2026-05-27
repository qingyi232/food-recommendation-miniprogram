package com.campus.food.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.food.entity.Merchant;
import com.campus.food.mapper.MerchantMapper;
import com.campus.food.service.MerchantService;
import com.campus.food.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(String username, String password) {
        String md5Password = DigestUtil.md5Hex(password);
        Merchant merchant = this.getOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getUsername, username)
                .eq(Merchant::getPassword, md5Password));
        if (merchant == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (merchant.getStatus() == 2) {
            throw new RuntimeException("账号已被禁用");
        }
        if (merchant.getStatus() == 0) {
            throw new RuntimeException("账号待审核，请耐心等待");
        }
        String token = jwtUtil.generateToken(merchant.getId(), merchant.getUsername(), "merchant");
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        merchant.setPassword(null);
        result.put("merchantInfo", merchant);
        return result;
    }

    @Override
    public Merchant register(Merchant merchant) {
        Merchant existing = this.getOne(new LambdaQueryWrapper<Merchant>().eq(Merchant::getUsername, merchant.getUsername()));
        if (existing != null) {
            throw new RuntimeException("用户名已存在");
        }
        merchant.setPassword(DigestUtil.md5Hex(merchant.getPassword()));
        merchant.setStatus(0);
        merchant.setViolationCount(0);
        this.save(merchant);
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    public Merchant getMerchantInfo(Long merchantId) {
        Merchant merchant = this.getById(merchantId);
        if (merchant != null) {
            merchant.setPassword(null);
        }
        return merchant;
    }

    @Override
    public Merchant updateMerchantInfo(Merchant merchant) {
        merchant.setPassword(null);
        this.updateById(merchant);
        return this.getById(merchant.getId());
    }

    @Override
    public IPage<Merchant> getMerchantPage(int page, int size, String keyword, Integer status) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Merchant::getName, keyword)
                    .or().like(Merchant::getUsername, keyword)
                    .or().like(Merchant::getPhone, keyword);
        }
        if (status != null) {
            wrapper.eq(Merchant::getStatus, status);
        }
        wrapper.orderByDesc(Merchant::getCreateTime);
        IPage<Merchant> result = this.page(new Page<>(page, size), wrapper);
        result.getRecords().forEach(m -> m.setPassword(null));
        return result;
    }

    @Override
    public void updateMerchantStatus(Long merchantId, Integer status) {
        Merchant merchant = new Merchant();
        merchant.setId(merchantId);
        merchant.setStatus(status);
        this.updateById(merchant);
    }

    @Override
    public void addViolation(Long merchantId) {
        Merchant merchant = this.getById(merchantId);
        if (merchant != null) {
            merchant.setViolationCount(merchant.getViolationCount() + 1);
            if (merchant.getViolationCount() >= 3) {
                merchant.setStatus(2);
            }
            this.updateById(merchant);
        }
    }
}
