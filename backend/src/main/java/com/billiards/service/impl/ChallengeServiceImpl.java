
















package com.billiards.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billiards.common.BusinessException;
import com.billiards.common.Constants;
import com.billiards.dto.ChallengeDetailVO;
import com.billiards.dto.SignupUserVO;
import com.billiards.entity.Ballroom;
import com.billiards.entity.Challenge;
import com.billiards.entity.ChallengeSignup;
import com.billiards.entity.User;
import com.billiards.mapper.BallroomMapper;
import com.billiards.mapper.ChallengeMapper;
import com.billiards.mapper.ChallengeSignupMapper;
import com.billiards.mapper.UserMapper;
import com.billiards.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 约战服务实现
 */
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl extends ServiceImpl<ChallengeMapper, Challenge> implements ChallengeService {

    private final UserMapper userMapper;
    private final BallroomMapper ballroomMapper;
    private final ChallengeSignupMapper signupMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Challenge createChallenge(Long userId, Long ballroomId, Integer ballType,
                                     Integer formatType, Integer formatValue,
                                     LocalDateTime startTime,
                                     Integer maxPlayers, String remark) {
        // 校验用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 校验球房
        Ballroom ballroom = ballroomMapper.selectById(ballroomId);
        if (ballroom == null) {
            throw new BusinessException("球房不存在");
        }

        Challenge challenge = new Challenge();
        challenge.setInitiatorId(userId);
        challenge.setBallroomId(ballroomId);
        challenge.setBallType(ballType);
        challenge.setFormatType(formatType);
        challenge.setFormatValue(formatValue);
        challenge.setStartTime(startTime);
        challenge.setMaxPlayers(maxPlayers != null ? maxPlayers : 2);
        challenge.setRemark(remark != null ? remark : "");
        challenge.setStatus(Constants.CHALLENGE_PENDING);
        save(challenge);
        return challenge;
    }

    @Override
    public Page<Challenge> getChallengePage(Integer page, Integer size, Integer ballType, Integer status) {
        LambdaQueryWrapper<Challenge> wrapper = new LambdaQueryWrapper<Challenge>()
                .eq(status != null, Challenge::getStatus, status)
                .eq(ballType != null, Challenge::getBallType, ballType)
                .orderByDesc(Challenge::getStartTime);

        Page<Challenge> result = page(new Page<>(page, size), wrapper);

        // 填充发起人信息和球房信息
        for (Challenge c : result.getRecords()) {
            fillChallengeExtra(c);
        }
        return result;
    }

    @Override
    public ChallengeDetailVO getChallengeDetail(Long challengeId) {
        Challenge challenge = getById(challengeId);
        if (challenge == null) {
            throw new BusinessException("约战不存在");
        }

        ChallengeDetailVO vo = new ChallengeDetailVO();
        // 复制 Challenge 字段
        vo.setId(challenge.getId());
        vo.setInitiatorId(challenge.getInitiatorId());
        vo.setBallroomId(challenge.getBallroomId());
        vo.setBallType(challenge.getBallType());
        vo.setFormatType(challenge.getFormatType());
        vo.setFormatValue(challenge.getFormatValue());
        vo.setStartTime(challenge.getStartTime());
        vo.setMaxPlayers(challenge.getMaxPlayers());
        vo.setRemark(challenge.getRemark());
        vo.setStatus(challenge.getStatus());
        vo.setScoreInitiator(challenge.getScoreInitiator());
        vo.setScoreOpponent(challenge.getScoreOpponent());
        vo.setWinnerId(challenge.getWinnerId());
        vo.setCreateTime(challenge.getCreateTime());
        vo.setUpdateTime(challenge.getUpdateTime());

        // 填充发起人信息
        User initiator = userMapper.selectById(challenge.getInitiatorId());
        if (initiator != null) {
            vo.setInitiatorName(initiator.getNickname());
            vo.setInitiatorAvatar(initiator.getAvatarUrl());
        }

        // 填充球房信息
        Ballroom ballroom = ballroomMapper.selectById(challenge.getBallroomId());
        if (ballroom != null) {
            vo.setBallroomName(ballroom.getName());
            vo.setBallroomAddress(ballroom.getAddress());
        }

        // 填充报名列表
        List<ChallengeSignup> signups = signupMapper.selectList(
                new LambdaQueryWrapper<ChallengeSignup>()
                        .eq(ChallengeSignup::getChallengeId, challengeId)
                        .orderByAsc(ChallengeSignup::getCreateTime));
        List<SignupUserVO> signupVOs = signups.stream().map(s -> {
            SignupUserVO su = new SignupUserVO();
            su.setSignupId(s.getId());
            su.setUserId(s.getUserId());
            su.setStatus(s.getStatus());
            User u = userMapper.selectById(s.getUserId());
            if (u != null) {
                su.setNickname(u.getNickname());
                su.setAvatarUrl(u.getAvatarUrl());
                su.setLevelScore(u.getLevelScore());
                su.setLevelName(com.billiards.dto.UserStatsVO.getLevelName(u.getLevelScore()));
            }
            return su;
        }).collect(Collectors.toList());
        vo.setSignups(signupVOs);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelChallenge(Long userId, Long challengeId) {
        Challenge challenge = getById(challengeId);
        if (challenge == null) {
            throw new BusinessException("约战不存在");
        }
        if (!challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("只有发起人可以取消约战");
        }
        if (challenge.getStatus() != Constants.CHALLENGE_PENDING) {
            throw new BusinessException("当前状态不可取消");
        }
        challenge.setStatus(Constants.CHALLENGE_CANCELLED);
        updateById(challenge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishChallenge(Long challengeId, Long userId, Integer scoreInitiator,
                                Integer scoreOpponent, Long winnerId) {
        Challenge challenge = getById(challengeId);
        if (challenge == null) {
            throw new BusinessException("约战不存在");
        }
        // 仅发起人可以结束约战
        if (!challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("只有发起人可以结束约战");
        }
        if (challenge.getStatus() != Constants.CHALLENGE_IN_PROGRESS) {
            throw new BusinessException("当前状态不可完成");
        }

        challenge.setStatus(Constants.CHALLENGE_FINISHED);
        challenge.setScoreInitiator(scoreInitiator);
        challenge.setScoreOpponent(scoreOpponent);
        challenge.setWinnerId(winnerId);
        updateById(challenge);

        // 更新双方战绩和段位积分
        if (winnerId != null) {
            // 胜者 +10 积分
            User winner = userMapper.selectById(winnerId);
            if (winner != null) {
                winner.setWins(winner.getWins() + 1);
                winner.setLevelScore(winner.getLevelScore() + 10);
                userMapper.updateById(winner);
            }

            // 败者 -5 积分（最低0）
            Long loserId = challenge.getInitiatorId().equals(winnerId)
                    ? getOpponentId(challenge) : challenge.getInitiatorId();
            if (loserId != null) {
                User loser = userMapper.selectById(loserId);
                if (loser != null) {
                    loser.setLosses(loser.getLosses() + 1);
                    loser.setLevelScore(Math.max(0, loser.getLevelScore() - 5));
                    userMapper.updateById(loser);
                }
            }
        }
    }

    @Override
    public Page<Challenge> getMyChallenges(Long userId, Integer type, Integer page, Integer size) {
        LambdaQueryWrapper<Challenge> wrapper = new LambdaQueryWrapper<>();
        if (type == 1) {
            // 我发起的（待应战 + 进行中）
            wrapper.eq(Challenge::getInitiatorId, userId)
                    .in(Challenge::getStatus, Constants.CHALLENGE_PENDING, Constants.CHALLENGE_IN_PROGRESS);
        } else if (type == 2) {
            // 我参加的（通过报名表查询）
            List<ChallengeSignup> mySignups = signupMapper.selectList(
                    new LambdaQueryWrapper<ChallengeSignup>()
                            .eq(ChallengeSignup::getUserId, userId)
                            .eq(ChallengeSignup::getStatus, Constants.SIGNUP_CONFIRMED));
            List<Long> challengeIds = mySignups.stream()
                    .map(ChallengeSignup::getChallengeId)
                    .collect(Collectors.toList());
            if (challengeIds.isEmpty()) {
                return new Page<>(page, size);
            }
            wrapper.in(Challenge::getId, challengeIds)
                    .in(Challenge::getStatus, Constants.CHALLENGE_PENDING, Constants.CHALLENGE_IN_PROGRESS);
        } else {
            // 历史的（已完成 + 已取消）
            wrapper.and(w -> w.eq(Challenge::getInitiatorId, userId)
                            .or(oc -> oc.apply("id IN (SELECT challenge_id FROM challenge_signup WHERE user_id = {0})", userId)))
                    .in(Challenge::getStatus, Constants.CHALLENGE_FINISHED, Constants.CHALLENGE_CANCELLED);
        }
        wrapper.orderByDesc(Challenge::getCreateTime);

        Page<Challenge> result = page(new Page<>(page, size), wrapper);
        for (Challenge c : result.getRecords()) {
            fillChallengeExtra(c);
        }
        return result;
    }

    /**
     * 填充约战的额外信息（发起人昵称、球房名称）
     */
    private void fillChallengeExtra(Challenge challenge) {
        User initiator = userMapper.selectById(challenge.getInitiatorId());
        if (initiator != null) {
            challenge.setInitiatorName(initiator.getNickname());
            challenge.setInitiatorAvatar(initiator.getAvatarUrl());
        }
        Ballroom ballroom = ballroomMapper.selectById(challenge.getBallroomId());
        if (ballroom != null) {
            challenge.setBallroomName(ballroom.getName());
            challenge.setBallroomAddress(ballroom.getAddress());
        }
    }

    /**
     * 获取对手ID
     */
    private Long getOpponentId(Challenge challenge) {
        List<ChallengeSignup> confirmed = signupMapper.selectList(
                new LambdaQueryWrapper<ChallengeSignup>()
                        .eq(ChallengeSignup::getChallengeId, challenge.getId())
                        .eq(ChallengeSignup::getStatus, Constants.SIGNUP_CONFIRMED));
        if (!confirmed.isEmpty()) {
            return confirmed.get(0).getUserId();
        }
        return null;
    }
}
















