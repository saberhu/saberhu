
















package com.billiards.service;

import com.billiards.dto.SignupUserVO;

import java.util.List;

/**
 * 约战报名业务接口
 */
public interface ChallengeSignupService {

    /**
     * 报名约战
     */
    void signup(Long challengeId, Long userId);

    /**
     * 确认报名
     */
    void confirmSignup(Long challengeId, Long userId, Long signupId);

    /**
     * 拒绝报名
     */
    void rejectSignup(Long challengeId, Long userId, Long signupId);

    /**
     * 获取约战的报名列表
     */
    List<SignupUserVO> getSignupsByChallengeId(Long challengeId);

    /**
     * 检查用户是否已报名
     */
    boolean isUserSignedUp(Long challengeId, Long userId);
}
















