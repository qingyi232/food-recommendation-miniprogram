package com.campus.food.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("user_behavior")
public class UserBehavior {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String behaviorType;
    private String targetType;
    private Long targetId;
    private BigDecimal score;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
