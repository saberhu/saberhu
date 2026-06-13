





















package com.billiards.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 处理报名请求
 */
@Data
public class SignupHandleDTO {

    @NotNull(message = "报名ID不能为空")
    private Long signupId;
}






















