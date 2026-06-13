





package com.billiards.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 处理报名 DTO
 */
@Data
public class SignupHandleDTO {

    @NotNull(message = "报名ID不能为空")
    private Long signupId;

    @NotNull(message = "处理状态不能为空")
    private Integer status; // 1-确认 2-拒绝
}





