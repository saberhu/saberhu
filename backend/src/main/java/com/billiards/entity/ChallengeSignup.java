
















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

    /** 约战ID */
    private Long challengeId;

    /** 报名用户ID */
    private Long userId;

    /** 报名状态 0-待确认 1-已确认 2-已拒绝 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

















