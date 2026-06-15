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

    // ===== 以下字段兼容前端直接使用 =====

    /** 发起人名称（兼容前端） */
    public String getInitiatorName() {
        return initiator != null ? initiator.getNickname() : null;
    }

    /** 发起人头像（兼容前端） */
    public String getInitiatorAvatar() {
        return initiator != null ? initiator.getAvatarUrl() : null;
    }

    /** 发起人段位积分（兼容前端） */
    public Integer getInitiatorLevelScore() {
        return initiator != null ? initiator.getLevelScore() : null;
    }

    /** 球房名称（兼容前端） */
    public String getBallroomName() {
        return ballroom != null ? ballroom.getName() : null;
    }

    /** 球房地址（兼容前端） */
    public String getBallroomAddress() {
        return ballroom != null ? ballroom.getAddress() : null;
    }

    /** 报名人数（兼容前端） */
    public Integer getSignupCount() {
        return signups != null ? signups.size() : 0;
    }

    /** 对手名称（已确认报名的用户） */
    public String getOpponentName() {
        if (signups != null) {
            for (SignupUserVO s : signups) {
                if (s.getStatus() == 1) { // SIGNUP_CONFIRMED
                    return s.getNickname();
                }
            }
        }
        return null;
    }

    /** 对手段位积分 */
    public Integer getOpponentLevelScore() {
        if (signups != null) {
            for (SignupUserVO s : signups) {
                if (s.getStatus() == 1) {
                    return s.getLevelScore();
                }
            }
        }
        return null;
    }
}
