




package com.billiards.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String openid;

    private String nickname;

    private String avatarUrl;

    private String phone;

    private Integer gender;

    private Integer levelScore;

    private Integer wins;

    private Integer losses;

    private Integer creditScore;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}





