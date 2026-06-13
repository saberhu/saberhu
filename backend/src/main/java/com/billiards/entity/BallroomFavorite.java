








package com.billiards.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 球房收藏实体
 */
@Data
@TableName("ballroom_favorite")
public class BallroomFavorite {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long ballroomId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}








