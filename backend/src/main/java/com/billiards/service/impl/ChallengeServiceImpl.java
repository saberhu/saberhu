













package com.billiards.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.billiards.service.ChallengeSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 约战业务实现
 */
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeMapper challengeMapper;
    private final ChallengeSignupMapper signupMapper;
    private final ChallengeSignupService signupService;
    private final UserMapper userMapper;
    private final BallroomMapper ballroomMapper;

    @Override
    @Transactional
    public Challenge createChallenge(Long userId, Long ballroomId, Integer ballType,
                                     Integer formatType, Integer formatValue,
                                     LocalDateTime startTime, Integer maxPlayers, String remark) {
        // 校验用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        // 校验球房
        Ballroom ballroom = ballroomMapper.selectById(ballroomId);
        if (ballroom == null) {
            throw new BusinessException(404, "球房不存在");
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
        challengeMapper.insert(challenge);
        return challenge;
    }

    @Override
    public Page<Challenge> getChallengePage(Integer page, Integer size, Integer ballType, Integer status) {
        Page<Challenge> challengePage = new Page<>(page, size);
        LambdaQueryWrapper<Challenge> wrapper = new LambdaQueryWrapper<Challenge>()
                .orderByDesc(Challenge::getCreateTime);

        if (ballType != null) {
            wrapper.eq(Challenge::getBallType, ballType);
        }
        if (status != null) {
            wrapper.eq(Challenge::getStatus, status);
        }

        return challengeMapper.selectPage(challengePage, wrapper);
    }

    @Override
    public ChallengeDetailVO getChallengeDetail(Long id) {
        Challenge challenge = challengeMapper.selectById(id);
        if (challenge == null) {
            throw new BusinessException(404, "约战不存在");
        }

        ChallengeDetailVO vo = new ChallengeDetailVO();
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

        // 发起人信息
        User initiator = userMapper.selectById(challenge.getInitiatorId());
        vo.setInitiator(initiator);

        // 球房信息
        Ballroom ballroom = ballroomMapper.selectById(challenge.getBallroomId());
        vo.setBallroom(ballroom);

        // 报名列表
        List<SignupUserVO> signups = signupService.getSignupsByChallengeId(id);
        vo.setSignups(signups);

        return vo;
    }

    @Override
    @Transactional
    public void cancelChallenge(Long userId, Long challengeId) {
        Challenge challenge = challengeMapper.selectById(challengeId);
        if (challenge == null) {
            throw new BusinessException(404, "约战不存在");
        }
        if (!challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("仅发起人可以取消约战");
        }
        if (challenge.getStatus() != Constants.CHALLENGE_PENDING) {
            throw new BusinessException("当前状态不允许取消");
        }
        challenge.setStatus(Constants.CHALLENGE_CANCELED);
        challengeMapper.updateById(challenge);
    }

    @Override
    @Transactional
    public void finishChallenge(Long challengeId, Long userId, Integer scoreInitiator,
                                Integer scoreOpponent, Long winnerId) {
        Challenge challenge = challengeMapper.selectById(challengeId);
        if (challenge == null) {
            throw new BusinessException(404, "约战不存在");
        }
        if (!challenge.getInitiatorId().equals(userId)) {
            throw new BusinessException("仅发起人可以提交比分");
        }
        if (challenge.getStatus() != Constants.CHALLENGE_ONGOING) {
            throw new BusinessException("当前状态不允许提交比分");
        }

        challenge.setScoreInitiator(scoreInitiator);
        challenge.setScoreOpponent(scoreOpponent);
        challenge.setWinnerId(winnerId);
        challenge.setStatus(Constants.CHALLENGE_FINISHED);
        challengeMapper.updateById(challenge);

        // 更新双方战绩
        if (winnerId != null) {
            User winner = userMapper.selectById(winnerId);
            if (winner != null) {
                winner.setWins(winner.getWins() + 1);
                winner.setLevelScore(winner.getLevelScore() + 10);
                userMapper.updateById(winner);
            }
            // 输家扣分
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
        Page<Challenge> challengePage = new Page<>(page, size);
        LambdaQueryWrapper<Challenge> wrapper = new LambdaQueryWrapper<Challenge>()
                .orderByDesc(Challenge::getCreateTime);

        switch (type) {
            case 1: // 我发起的
                wrapper.eq(Challenge::getInitiatorId, userId);
                break;
            case 2: { // 我参加的
                List<Long> challengeIds = signupMapper.selectList(
                        new LambdaQueryWrapper<ChallengeSignup>()
                                .eq(ChallengeSignup::getUserId, userId)
                                .eq(ChallengeSignup::getStatus, Constants.SIGNUP_CONFIRMED))
                        .stream().map(ChallengeSignup::getChallengeId)
                        .collect(Collectors.toList());
                if (challengeIds.isEmpty()) {
                    return new Page<>(page, size, 0);
                }
                wrapper.in(Challenge::getId, challengeIds);
                break;
            }
            case 3: // 历史的（我发起或参加的已完成/已取消）
                wrapper.and(w -> w.eq(Challenge::getInitiatorId, userId)
                        .or().in(Challenge::getId,
                                signupMapper.selectList(
                                        new LambdaQueryWrapper<ChallengeSignup>()
                                                .eq(ChallengeSignup::getUserId, userId))
                                        .stream().map(ChallengeSignup::getChallengeId)
                                        .collect(Collectors.toList())));
                wrapper.in(Challenge::getStatus, Constants.CHALLENGE_FINISHED, Constants.CHALLENGE_CANCELED);
                break;
            default:
                break;
        }

        return challengeMapper.selectPage(challengePage, wrapper);
    }

    /** 获取对手ID */
    private Long getOpponentId(Challenge challenge) {
        List<ChallengeSignup> signups = signupMapper.selectList(
                new LambdaQueryWrapper<ChallengeSignup>()
                        .eq(ChallengeSignup::getChallengeId, challenge.getId())
                        .eq(ChallengeSignup::getStatus, Constants.SIGNUP_CONFIRMED));
        return signups.isEmpty() ? null : signups.get(0).getUserId();
    }
}













