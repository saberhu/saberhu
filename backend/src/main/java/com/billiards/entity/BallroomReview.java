








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

    private Long ballroomId;

    private Long userId;

    private BigDecimal rating;

    private String content;

    private String images;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}







