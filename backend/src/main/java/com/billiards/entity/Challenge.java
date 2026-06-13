












package com.billiards.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 约战实体
 */
@Data
@TableName("challenge")
public class Challenge {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发起人用户ID */
    private Long initiatorId;

    /** 球房ID */
    private Long ballroomId;

    /** 球种 1-中式八球 2-斯诺克 3-九球 */
    private Integer ballType;

    /** 赛制 1-抢X 2-限时 */
    private Integer formatType;

    /** 赛制值，抢X时为局数，限时时为分钟数 */
    private Integer formatValue;

    /** 约定开始时间 */
    private LocalDateTime startTime;

    /** 最大参与人数 */
    private Integer maxPlayers;

    /** 备注说明 */
    private String remark;

    /** 状态 0-待应战 1-进行中 2-已完成 3-已取消 */
    private Integer status;

    /** 发起人得分 */
    private Integer scoreInitiator;

    /** 对手得分 */
    private Integer scoreOpponent;

    /** 胜者用户ID */
    private Long winnerId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}













