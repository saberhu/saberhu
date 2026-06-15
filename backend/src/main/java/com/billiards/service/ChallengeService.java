package com.billiards.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billiards.dto.ChallengeDetailVO;
import com.billiards.dto.ChallengeListVO;
import com.billiards.entity.Challenge;

import java.time.LocalDateTime;

/**
 * 约战业务接口
 */
public interface ChallengeService {

    Challenge createChallenge(Long userId, Long ballroomId, Integer ballType,
                              Integer formatType, Integer formatValue,
                              LocalDateTime startTime, Integer maxPlayers, String remark);

    Page<ChallengeListVO> getChallengePage(Integer page, Integer size, Integer ballType, Integer status);

    ChallengeDetailVO getChallengeDetail(Long id);

    void cancelChallenge(Long userId, Long challengeId);

    void finishChallenge(Long challengeId, Long userId, Integer scoreInitiator,
                         Integer scoreOpponent, Long winnerId);

    Page<Challenge> getMyChallenges(Long userId, Integer type, Integer page, Integer size);
}
