package com.campus.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("dish")
public class Dish {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long shopId;
    private String name;
    private String image;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Long categoryId;
    private String tags;
    private Integer spicyLevel;
    private Integer sales;
    private BigDecimal rating;
    private Integer status;
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private String shopName;
    @TableField(exist = false)
    private String categoryName;
    @TableField(exist = false)
    private Boolean isFavorite;
}
