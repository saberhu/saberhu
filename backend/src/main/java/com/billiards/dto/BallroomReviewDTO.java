








package com.billiards.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 球房评价 DTO
 */
@Data
public class BallroomReviewDTO {

    @NotNull(message = "球房不能为空")
    private Long ballroomId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低1分")
    @Max(value = 5, message = "评分最高5分")
    private Double rating;

    private String content;

    private String images;
}









