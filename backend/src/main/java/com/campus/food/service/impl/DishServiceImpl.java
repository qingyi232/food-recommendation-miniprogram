package com.campus.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.food.entity.Dish;
import com.campus.food.mapper.DishMapper;
import com.campus.food.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public IPage<Dish> getDishPage(int page, int size, Long shopId, Long categoryId, String keyword) {
        return dishMapper.selectDishPage(new Page<>(page, size), shopId, categoryId, keyword);
    }

    @Override
    public Dish getDishDetail(Long dishId) {
        return this.getById(dishId);
    }

    @Override
    public Dish addDish(Dish dish) {
        this.save(dish);
        return dish;
    }

    @Override
    public Dish updateDish(Dish dish) {
        this.updateById(dish);
        return this.getById(dish.getId());
    }

    @Override
    public void updateDishStatus(Long dishId, Integer status) {
        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setStatus(status);
        this.updateById(dish);
    }

    @Override
    public List<Dish> getShopDishes(Long shopId) {
        return this.list(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getShopId, shopId)
                .eq(Dish::getStatus, 1)
                .orderByAsc(Dish::getSortOrder));
    }

    @Override
    public List<Dish> getDishByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return this.listByIds(ids);
    }
}
