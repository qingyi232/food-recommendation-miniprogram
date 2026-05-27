package com.campus.food.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.food.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Select("<script>" +
            "SELECT d.*, s.name as shop_name, c.name as category_name " +
            "FROM dish d " +
            "LEFT JOIN shop s ON d.shop_id = s.id " +
            "LEFT JOIN category c ON d.category_id = c.id " +
            "<where>" +
            "<if test='shopId != null'> AND d.shop_id = #{shopId}</if>" +
            "<if test='categoryId != null'> AND d.category_id = #{categoryId}</if>" +
            "<if test='keyword != null and keyword != \"\"'> AND (d.name LIKE CONCAT('%',#{keyword},'%') OR d.tags LIKE CONCAT('%',#{keyword},'%'))</if>" +
            " AND d.status = 1" +
            "</where>" +
            " ORDER BY d.sort_order ASC, d.sales DESC" +
            "</script>")
    IPage<Dish> selectDishPage(Page<Dish> page, @Param("shopId") Long shopId,
                               @Param("categoryId") Long categoryId, @Param("keyword") String keyword);

    @Select("SELECT d.*, s.name as shop_name FROM dish d LEFT JOIN shop s ON d.shop_id = s.id " +
            "WHERE d.id IN (SELECT dish_id FROM favorite_food WHERE user_id = #{userId})")
    List<Dish> selectFavoriteDishes(@Param("userId") Long userId);
}
