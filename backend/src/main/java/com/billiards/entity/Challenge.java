





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

    private Long initiatorId;

    private Long ballroomId;

    private Integer ballType;

    private Integer formatType;

    private Integer formatValue;

    private LocalDateTime startTime;

    private Integer maxPlayers;

    private String remark;

    private Integer status;

    private Integer scoreInitiator;

    private Integer scoreOpponent;

    private Long winnerId;

    /** 发起人昵称（非数据库字段） */
    @TableField(exist = false)
    private String initiatorName;

    /** 发起人头像（非数据库字段） */
    @TableField(exist = false)
    private String initiatorAvatar;

    /** 球房名称（非数据库字段） */
    @TableField(exist = false)
    private String ballroomName;

    /** 球房地址（非数据库字段） */
    @TableField(exist = false)
    private String ballroomAddress;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}





