








package com.billiards.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 微信登录请求
 */
@Data
public class UserLoginDTO {

    @NotBlank(message = "code不能为空")
    private String code;

    private String nickname;
    private String avatarUrl;
}








