package com.campus.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.food.entity.Category;
import com.campus.food.entity.Merchant;
import com.campus.food.entity.Shop;
import com.campus.food.mapper.CategoryMapper;
import com.campus.food.mapper.MerchantMapper;
import com.campus.food.mapper.ShopMapper;
import com.campus.food.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public IPage<Shop> getShopPage(int page, int size, Long categoryId, String keyword, Long merchantId) {
        return shopMapper.selectShopPage(new Page<>(page, size), categoryId, keyword, merchantId);
    }

    @Override
    public Shop getShopDetail(Long shopId) {
        Shop shop = this.getById(shopId);
        if (shop != null) {
            Category category = categoryMapper.selectById(shop.getCategoryId());
            if (category != null) {
                shop.setCategoryName(category.getName());
            }
            Merchant merchant = merchantMapper.selectById(shop.getMerchantId());
            if (merchant != null) {
                shop.setMerchantName(merchant.getName());
            }
        }
        return shop;
    }

    @Override
    public Shop addShop(Shop shop) {
        this.save(shop);
        return shop;
    }

    @Override
    public Shop updateShop(Shop shop) {
        this.updateById(shop);
        return this.getById(shop.getId());
    }

    @Override
    public void updateShopStatus(Long shopId, Integer status) {
        Shop shop = new Shop();
        shop.setId(shopId);
        shop.setStatus(status);
        this.updateById(shop);
    }

    @Override
    public List<Shop> getMerchantShops(Long merchantId) {
        return this.list(new LambdaQueryWrapper<Shop>()
                .eq(Shop::getMerchantId, merchantId)
                .orderByDesc(Shop::getCreateTime));
    }

    @Override
    public List<Shop> getShopsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return this.listByIds(ids);
    }
}
