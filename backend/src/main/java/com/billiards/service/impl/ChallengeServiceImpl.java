
















package com.billiards.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billiards.common.BusinessException;
import com.billiards.common.Constants;
import com.billiards.entity.Challenge;
import com.billiards.entity.User;
import com.billiards.mapper.ChallengeMapper;
import com.billiards.mapper.UserMapper;
import com.billiards.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 约战服务实现
 */
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl extends ServiceImpl<ChallengeMapper, Challenge> implements ChallengeService {

    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Challenge createChallenge(Long userId, Long ballroomId, Integer ballType,
                                     Integer formatType, Integer formatValue,
                                     LocalDateTime startTime,
                                     Integer maxPlayers, String remark) {
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
    public void finishChallenge(Long challengeId, Integer scoreInitiator,
                                Integer scoreOpponent, Long winnerId) {
        Challenge challenge = getById(challengeId);
        if (challenge == null) {
            throw new BusinessException("约战不存在");
        }
        if (challenge.getStatus() != Constants.CHALLENGE_IN_PROGRESS) {
            throw new BusinessException("当前状态不可完成");
        }

        challenge.setStatus(Constants.CHALLENGE_FINISHED);
        challenge.setScoreInitiator(scoreInitiator);
        challenge.setScoreOpponent(scoreOpponent);
        challenge.setWinnerId(winnerId);
        updateById(challenge);

        // 更新用户胜负数
        if (winnerId != null) {
            User winner = userMapper.selectById(winnerId);
            if (winner != null) {
                winner.setWins(winner.getWins() + 1);
                winner.setLevelScore(winner.getLevelScore() + 10);
                userMapper.updateById(winner);
            }
            // 对手负场由发起人/对手关系决定，简化处理
            Long loserId = challenge.getInitiatorId().equals(winnerId)
                    ? null : challenge.getInitiatorId();
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
}
















