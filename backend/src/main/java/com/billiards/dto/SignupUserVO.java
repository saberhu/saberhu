





package com.billiards.dto;

import lombok.Data;

/**
 * 报名用户信息 VO
 */
@Data
public class SignupUserVO {

    private Long signupId;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private Integer levelScore;
    private String levelName;
    private Integer status;
}





