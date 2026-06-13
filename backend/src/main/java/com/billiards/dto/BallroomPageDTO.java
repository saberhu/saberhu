
































package com.billiards.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 球房列表返回
 */
@Data
public class BallroomPageDTO {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String priceDesc;
    private String businessHours;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String images;
    private BigDecimal rating;
    private Integer ratingCount;
    private Boolean isFavorite;
}


































