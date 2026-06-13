










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

    /** 球房名称 */
    private String name;

    /** 详细地址 */
    private String address;

    /** 联系电话 */
    private String phone;

    /** 价格描述 */
    private String priceDesc;

    /** 营业时间 */
    private String businessHours;

    /** 经度 */
    private BigDecimal longitude;

    /** 纬度 */
    private BigDecimal latitude;

    /** 图片列表，JSON数组 */
    private String images;

    /** 综合评分 */
    private BigDecimal rating;

    /** 评价人数 */
    private Integer ratingCount;

    /** 状态 1-营业 0-歇业 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}











