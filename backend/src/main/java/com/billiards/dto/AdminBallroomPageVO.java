



package com.billiards.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理员-球房列表视图
 */
@Data
public class AdminBallroomPageVO {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String priceDesc;
    private String businessHours;
    private BigDecimal rating;
    private Integer ratingCount;
    private Integer status;
    private Integer totalChallenges;
    private LocalDateTime createTime;
}


