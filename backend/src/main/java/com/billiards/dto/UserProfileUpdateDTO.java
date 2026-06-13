











package com.billiards.dto;

import lombok.Data;

/**
 * 用户资料更新请求
 */
@Data
public class UserProfileUpdateDTO {

    private String nickname;
    private String avatarUrl;
    private String phone;
    private Integer gender;
}











