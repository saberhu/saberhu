











package com.billiards.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billiards.entity.Challenge;

/**
 * 约战服务接口
 */
public interface ChallengeService extends IService<Challenge> {

    /**
     * 创建约战
     */
    Challenge createChallenge(Long userId, Long ballroomId, Integer ballType,
                              Integer formatType, Integer formatValue,
                              java.time.LocalDateTime startTime,
                              Integer maxPlayers, String remark);

    /**
     * 取消约战
     */
    void cancelChallenge(Long userId, Long challengeId);

    /**
     * 完成约战并记录比分
     */
    void finishChallenge(Long challengeId, Integer scoreInitiator,
                         Integer scoreOpponent, Long winnerId);
}











