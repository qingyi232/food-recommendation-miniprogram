package com.campus.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long shopId;
    private Long dishId;
    private Long orderId;
    private String content;
    private Integer rating;
    private String images;
    private Integer tasteRating;
    private Integer serviceRating;
    private Integer environmentRating;
    private Integer status;
    private String reply;
    private Date replyTime;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String userAvatar;
    @TableField(exist = false)
    private String shopName;
    @TableField(exist = false)
    private String dishName;
}
