


















package com.billiards.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billiards.common.BusinessException;
import com.billiards.common.Constants;
import com.billiards.entity.Challenge;
import com.billiards.entity.ChallengeSignup;
import com.billiards.mapper.ChallengeMapper;
import com.billiards.mapper.ChallengeSignupMapper;
import com.billiards.service.ChallengeSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 约战报名服务实现
 */
@Service
@RequiredArgsConstructor
public class ChallengeSignupServiceImpl extends ServiceImpl<ChallengeSignupMapper, ChallengeSignup>
        implements ChallengeSignupService {

    private final ChallengeMapper challengeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signup(Long challengeId, Long userId) {
        Challenge challenge = challengeMapper.selectById(challengeId);
        if (challenge == null) {
            throw new BusinessException("约战不存在");
        }
        if (challenge.getStatus() != Constants.CHALLENGE_PENDING) {
            throw new BusinessException("该约战已结束，无法报名");
        }
        // 发起人不能报名自己的约战
        if (challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("发起人无需报名");
        }

        // 检查是否已报名
        Long count = lambdaQuery()
                .eq(ChallengeSignup::getChallengeId, challengeId)
                .eq(ChallengeSignup::getUserId, userId)
                .count();
        if (count > 0) {
            throw new BusinessException("您已报名该约战");
        }

        ChallengeSignup signup = new ChallengeSignup();
        signup.setChallengeId(challengeId);
        signup.setUserId(userId);
        signup.setStatus(Constants.SIGNUP_PENDING);
        save(signup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmSignup(Long challengeId, Long userId, Long signupId) {
        // 校验发起人身份
        Challenge challenge = challengeMapper.selectById(challengeId);
        if (challenge == null) {
            throw new BusinessException("约战不存在");
        }
        if (!challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("只有发起人可以确认报名");
        }

        ChallengeSignup signup = getById(signupId);
        if (signup == null) {
            throw new BusinessException("报名记录不存在");
        }
        if (!signup.getChallengeId().equals(challengeId)) {
            throw new BusinessException("报名记录不属于该约战");
        }
        if (signup.getStatus() != Constants.SIGNUP_PENDING) {
            throw new BusinessException("该报名已处理");
        }

        signup.setStatus(Constants.SIGNUP_CONFIRMED);
        updateById(signup);

        // 确认报名后更新约战状态为进行中
        if (challenge.getStatus() == Constants.CHALLENGE_PENDING) {
            challenge.setStatus(Constants.CHALLENGE_IN_PROGRESS);
            challengeMapper.updateById(challenge);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectSignup(Long challengeId, Long userId, Long signupId) {
        // 校验发起人身份
        Challenge challenge = challengeMapper.selectById(challengeId);
        if (challenge == null) {
            throw new BusinessException("约战不存在");
        }
        if (!challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("只有发起人可以拒绝报名");
        }

        ChallengeSignup signup = getById(signupId);
        if (signup == null) {
            throw new BusinessException("报名记录不存在");
        }
        if (!signup.getChallengeId().equals(challengeId)) {
            throw new BusinessException("报名记录不属于该约战");
        }
        if (signup.getStatus() != Constants.SIGNUP_PENDING) {
            throw new BusinessException("该报名已处理");
        }

        signup.setStatus(Constants.SIGNUP_REJECTED);
        updateById(signup);
    }
}


















