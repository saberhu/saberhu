





package com.billiards.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录 DTO
 */
@Data
public class UserLoginDTO {

    @NotBlank(message = "code不能为空")
    private String code;  // 微信登录临时code
}





