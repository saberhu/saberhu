



























package com.billiards.dto;

import com.billiards.entity.Ballroom;
import com.billiards.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 约战详情（含关联信息）
 */
@Data
public class ChallengeDetailVO {

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

    /** 发起人信息 */
    private User initiator;

    /** 球房信息 */
    private Ballroom ballroom;

    /** 报名列表 */
    private List<SignupUserVO> signups;

    /** 当前用户是否已报名 */
    private Boolean isSignedUp;

    /** 当前用户是否是发起人 */
    private Boolean isInitiator;
}





























