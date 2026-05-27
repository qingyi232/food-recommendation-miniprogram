package com.campus.food.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.food.entity.FavoriteFood;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteFoodMapper extends BaseMapper<FavoriteFood> {
}
