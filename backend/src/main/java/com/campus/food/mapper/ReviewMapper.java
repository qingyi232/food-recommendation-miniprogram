package com.campus.food.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.food.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    @Select("<script>" +
            "SELECT r.*, u.nickname as user_name, u.avatar as user_avatar, " +
            "s.name as shop_name, d.name as dish_name " +
            "FROM review r " +
            "LEFT JOIN user u ON r.user_id = u.id " +
            "LEFT JOIN shop s ON r.shop_id = s.id " +
            "LEFT JOIN dish d ON r.dish_id = d.id " +
            "<where>" +
            "<if test='shopId != null'> AND r.shop_id = #{shopId}</if>" +
            "<if test='userId != null'> AND r.user_id = #{userId}</if>" +
            "<if test='status != null'> AND r.status = #{status}</if>" +
            "</where>" +
            " ORDER BY r.create_time DESC" +
            "</script>")
    IPage<Review> selectReviewPage(Page<Review> page, @Param("shopId") Long shopId,
                                   @Param("userId") Long userId, @Param("status") Integer status);
}
