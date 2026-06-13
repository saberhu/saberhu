



package com.billiards.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 球房列表查询 DTO
 */
@Data
public class BallroomPageDTO {

    private Integer page = 1;

    private Integer size = 10;

    /** 排序方式：rating-按评分，distance-按距离 */
    private String sortBy;

    /** 用户纬度（距离排序时必传） */
    private BigDecimal latitude;

    /** 用户经度（距离排序时必传） */
    private BigDecimal longitude;
}



