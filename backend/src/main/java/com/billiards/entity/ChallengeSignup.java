






package com.billiards.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 约战报名实体
 */
@Data
@TableName("challenge_signup")
public class ChallengeSignup {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long challengeId;

    private Long userId;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}






