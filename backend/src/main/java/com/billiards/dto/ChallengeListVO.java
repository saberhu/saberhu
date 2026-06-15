package com.billiards.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 约战列表返回（含发起人名称、球房名称等）
 */
@Data
public class ChallengeListVO {

    private Long id;
    private Long initiatorId;
    private Long ballroomId;
    private Integer ballType;
    private Integer formatType;
    private Integer formatValue;
    private LocalDateTime startTime;
    private Integer maxPlayers;
    private String remark;
    private Integer status;
    private Integer scoreInitiator;
    private Integer scoreOpponent;
    private Long winnerId;
    private LocalDateTime createTime;

    /** 发起人名称 */
    private String initiatorName;
    /** 发起人段位积分 */
    private Integer initiatorLevelScore;
    /** 球房名称 */
    private String ballroomName;
    /** 报名人数 */
    private Integer signupCount;
}
