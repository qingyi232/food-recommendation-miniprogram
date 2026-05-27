package com.campus.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("shop")
public class Shop {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private String name;
    private String logo;
    private String images;
    private String description;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String phone;
    private String businessHours;
    private BigDecimal avgPrice;
    private BigDecimal rating;
    private Integer totalSales;
    private Long categoryId;
    private String tags;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private String categoryName;
    @TableField(exist = false)
    private String merchantName;
    @TableField(exist = false)
    private Boolean isFavorite;
    @TableField(exist = false)
    private Integer reviewCount;
}
