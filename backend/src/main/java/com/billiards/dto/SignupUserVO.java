
























package com.billiards.dto;

import lombok.Data;

/**
 * 报名用户信息
 */
@Data
public class SignupUserVO {

    private Long signupId;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private Integer levelScore;
    private Integer status; // 0-待确认 1-已确认 2-已拒绝
}

























