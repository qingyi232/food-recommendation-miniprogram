package com.campus.food.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.food.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    @Select("<script>" +
            "SELECT o.*, s.name as shop_name, u.nickname as user_name " +
            "FROM order_info o " +
            "LEFT JOIN shop s ON o.shop_id = s.id " +
            "LEFT JOIN user u ON o.user_id = u.id " +
            "<where>" +
            "<if test='userId != null'> AND o.user_id = #{userId}</if>" +
            "<if test='merchantId != null'> AND o.merchant_id = #{merchantId}</if>" +
            "<if test='status != null'> AND o.status = #{status}</if>" +
            "</where>" +
            " ORDER BY o.create_time DESC" +
            "</script>")
    IPage<OrderInfo> selectOrderPage(Page<OrderInfo> page, @Param("userId") Long userId,
                                     @Param("merchantId") Long merchantId, @Param("status") Integer status);
}
