




package com.billiards.dto;

import com.billiards.entity.Challenge;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 约战详情 VO（含发起人、球房、报名信息）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChallengeDetailVO extends Challenge {

    /** 发起人昵称 */
    private String initiatorName;

    /** 发起人头像 */
    private String initiatorAvatar;

    /** 球房名称 */
    private String ballroomName;

    /** 球房地址 */
    private String ballroomAddress;

    /** 报名列表 */
    private List<SignupUserVO> signups;
}




