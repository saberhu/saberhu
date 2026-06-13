



package com.billiards.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户资料更新 DTO
 */
@Data
public class UserProfileUpdateDTO {

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    private String avatarUrl;

    private Integer gender;
}


