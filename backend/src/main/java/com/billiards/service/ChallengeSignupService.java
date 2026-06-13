













package com.billiards.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billiards.entity.ChallengeSignup;

/**
 * 约战报名服务接口
 */
public interface ChallengeSignupService extends IService<ChallengeSignup> {

    /**
     * 报名约战
     */
    void signup(Long challengeId, Long userId);

    /**
     * 确认报名（仅发起人）
     */
    void confirmSignup(Long challengeId, Long userId, Long signupId);

    /**
     * 拒绝报名（仅发起人）
     */
    void rejectSignup(Long challengeId, Long userId, Long signupId);
}













