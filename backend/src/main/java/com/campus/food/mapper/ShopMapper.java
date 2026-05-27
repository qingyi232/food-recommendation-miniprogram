package com.campus.food.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.food.entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ShopMapper extends BaseMapper<Shop> {

    @Select("<script>" +
            "SELECT s.*, c.name as category_name, m.name as merchant_name, " +
            "(SELECT COUNT(*) FROM review r WHERE r.shop_id = s.id AND r.status = 1) as review_count " +
            "FROM shop s " +
            "LEFT JOIN category c ON s.category_id = c.id " +
            "LEFT JOIN merchant m ON s.merchant_id = m.id " +
            "<where>" +
            "<if test='categoryId != null'> AND s.category_id = #{categoryId}</if>" +
            "<if test='keyword != null and keyword != \"\"'> AND (s.name LIKE CONCAT('%',#{keyword},'%') OR s.tags LIKE CONCAT('%',#{keyword},'%'))</if>" +
            "<if test='merchantId != null'> AND s.merchant_id = #{merchantId}</if>" +
            " AND s.status = 1" +
            "</where>" +
            " ORDER BY s.rating DESC, s.total_sales DESC" +
            "</script>")
    IPage<Shop> selectShopPage(Page<Shop> page, @Param("categoryId") Long categoryId,
                               @Param("keyword") String keyword, @Param("merchantId") Long merchantId);
}
