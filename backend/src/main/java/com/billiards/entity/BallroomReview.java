


















package com.billiards.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 球房评价实体
 */
@Data
@TableName("ballroom_review")
public class BallroomReview {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 球房ID */
    private Long ballroomId;

    /** 用户ID */
    private Long userId;

    /** 评分 1.0-5.0 */
    private BigDecimal rating;

    /** 评价内容 */
    private String content;

    /** 图片URL列表，JSON数组 */
    private String images;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}



















