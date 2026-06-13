





package com.billiards.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 结束约战提交比分 DTO
 */
@Data
public class ChallengeFinishDTO {

    @NotNull(message = "发起人得分不能为空")
    private Integer scoreInitiator;

    @NotNull(message = "对手得分不能为空")
    private Integer scoreOpponent;

    private Long winnerId;
}





