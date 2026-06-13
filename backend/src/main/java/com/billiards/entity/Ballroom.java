





package com.billiards.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 球房实体
 */
@Data
@TableName("ballroom")
public class Ballroom {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String priceDesc;

    private String businessHours;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String images;

    private BigDecimal rating;

    private Integer ratingCount;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}





