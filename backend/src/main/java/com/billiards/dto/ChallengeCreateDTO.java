
















package com.billiards.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 创建约战请求
 */
@Data
public class ChallengeCreateDTO {

    @NotNull(message = "球房不能为空")
    private Long ballroomId;

    @NotNull(message = "球种不能为空")
    private Integer ballType;

    @NotNull(message = "赛制不能为空")
    private Integer formatType;

    @NotNull(message = "赛制值不能为空")
    private Integer formatValue;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    private Integer maxPlayers = 2;
    private String remark;
}
















