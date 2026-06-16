


package com.billiards.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员-用户列表视图
 */
@Data
public class AdminUserPageVO {

    private Long id;
    private String nickname;
    private String phone;
    private Integer gender;
    private Integer levelScore;
    private Integer wins;
    private Integer losses;
    private Integer creditScore;
    private Integer status;
    private Integer totalChallenges;
    private LocalDateTime createTime;
}


