

package com.billiards.dto;

import lombok.Data;

/**
 * 管理员登录响应
 */
@Data
public class AdminLoginVO {

    private Long adminId;
    private String token;
    private String nickname;
    private String role;
}

