








package com.billiards.dto;

import lombok.Data;

/**
 * 微信登录返回
 */
@Data
public class UserLoginVO {

    private String token;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private Integer levelScore;
}








