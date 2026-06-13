

package com.billiards.dto;

import lombok.Data;

/**
 * 用户登录返回 VO
 */
@Data
public class UserLoginVO {

    private Long userId;
    private String token;
    private String nickname;
    private String avatarUrl;
    private Integer levelScore;
    private Integer wins;
    private Integer losses;
    private Integer creditScore;
}

