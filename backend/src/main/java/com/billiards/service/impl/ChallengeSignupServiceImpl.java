package com.billiards.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.billiards.common.BusinessException;
import com.billiards.common.Constants;
import com.billiards.dto.SignupUserVO;
import com.billiards.entity.Challenge;
import com.billiards.entity.ChallengeSignup;
import com.billiards.entity.User;
import com.billiards.mapper.ChallengeMapper;
import com.billiards.mapper.ChallengeSignupMapper;
import com.billiards.mapper.UserMapper;
import com.billiards.service.ChallengeSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 约战报名业务实现
 */
@Service
@RequiredArgsConstructor
public class ChallengeSignupServiceImpl implements ChallengeSignupService {

    private final ChallengeSignupMapper signupMapper;
    private final ChallengeMapper challengeMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void signup(Long challengeId, Long userId) {
        Challenge challenge = challengeMapper.selectById(challengeId);
        if (challenge == null) {
            throw new BusinessException(404, "约战不存在");
        }
        if (challenge.getStatus() != Constants.CHALLENGE_PENDING) {
            throw new BusinessException("当前约战状态不允许报名");
        }
        if (challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("发起人无需报名");
        }

        ChallengeSignup existing = signupMapper.selectOne(
                new LambdaQueryWrapper<ChallengeSignup>()
                        .eq(ChallengeSignup::getChallengeId, challengeId)
                        .eq(ChallengeSignup::getUserId, userId));
        if (existing != null) {
            throw new BusinessException("您已报名该约战");
        }

        ChallengeSignup signup = new ChallengeSignup();
        signup.setChallengeId(challengeId);
        signup.setUserId(userId);
        signup.setStatus(Constants.SIGNUP_PENDING);
        signupMapper.insert(signup);
    }

    @Override
    @Transactional
    public void cancelSignup(Long challengeId, Long userId) {
        ChallengeSignup signup = signupMapper.selectOne(
                new LambdaQueryWrapper<ChallengeSignup>()
                        .eq(ChallengeSignup::getChallengeId, challengeId)
                        .eq(ChallengeSignup::getUserId, userId));
        if (signup == null) {
            throw new BusinessException(404, "报名记录不存在");
        }
        if (signup.getStatus() == Constants.SIGNUP_CONFIRMED) {
            throw new BusinessException("已确认的报名无法取消，请联系发起人");
        }
        signupMapper.deleteById(signup.getId());
    }

    @Override
    @Transactional
    public void confirmSignup(Long challengeId, Long userId, Long signupId) {
        Challenge challenge = challengeMapper.selectById(challengeId);
        if (challenge == null || !challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("仅发起人可以确认报名");
        }

        ChallengeSignup signup = signupMapper.selectById(signupId);
        if (signup == null || !signup.getChallengeId().equals(challengeId)) {
            throw new BusinessException("报名记录不存在");
        }

        signup.setStatus(Constants.SIGNUP_CONFIRMED);
        signupMapper.updateById(signup);

        if (challenge.getStatus() == Constants.CHALLENGE_PENDING) {
            challenge.setStatus(Constants.CHALLENGE_ONGOING);
            challengeMapper.updateById(challenge);
        }
    }

    @Override
    @Transactional
    public void rejectSignup(Long challengeId, Long userId, Long signupId) {
        Challenge challenge = challengeMapper.selectById(challengeId);
        if (challenge == null || !challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("仅发起人可以拒绝报名");
        }

        ChallengeSignup signup = signupMapper.selectById(signupId);
        if (signup == null || !signup.getChallengeId().equals(challengeId)) {
            throw new BusinessException("报名记录不存在");
        }

        signup.setStatus(Constants.SIGNUP_REJECTED);
        signupMapper.updateById(signup);
    }

    @Override
    public List<SignupUserVO> getSignupsByChallengeId(Long challengeId) {
        List<ChallengeSignup> signups = signupMapper.selectList(
                new LambdaQueryWrapper<ChallengeSignup>()
                        .eq(ChallengeSignup::getChallengeId, challengeId));

        return signups.stream().map(s -> {
            SignupUserVO vo = new SignupUserVO();
            vo.setSignupId(s.getId());
            vo.setUserId(s.getUserId());
            vo.setStatus(s.getStatus());

            User user = userMapper.selectById(s.getUserId());
            if (user != null) {
                vo.setNickname(user.getNickname());
                vo.setAvatarUrl(user.getAvatarUrl());
                vo.setLevelScore(user.getLevelScore());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isUserSignedUp(Long challengeId, Long userId) {
        return signupMapper.selectCount(
                new LambdaQueryWrapper<ChallengeSignup>()
                        .eq(ChallengeSignup::getChallengeId, challengeId)
                        .eq(ChallengeSignup::getUserId, userId)) > 0;
    }
}
