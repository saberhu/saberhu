








package com.billiards.dto;

import lombok.Data;

/**
 * 约战列表查询 DTO
 */
@Data
public class ChallengePageDTO {

    private Integer status;

    private Long ballroomId;

    private Integer page = 1;

    private Integer size = 10;
}









