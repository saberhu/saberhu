













package com.billiards.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billiards.dto.ChallengeDetailVO;
import com.billiards.entity.Challenge;

import java.time.LocalDateTime;

/**
 * 约战业务接口
 */
public interface ChallengeService {

    /**
     * 创建约战
     */
    Challenge createChallenge(Long userId, Long ballroomId, Integer ballType,
                              Integer formatType, Integer formatValue,
                              LocalDateTime startTime, Integer maxPlayers, String remark);

    /**
     * 约战分页列表
     */
    Page<Challenge> getChallengePage(Integer page, Integer size, Integer ballType, Integer status);

    /**
     * 约战详情
     */
    ChallengeDetailVO getChallengeDetail(Long id);

    /**
     * 取消约战
     */
    void cancelChallenge(Long userId, Long challengeId);

    /**
     * 完成约战（提交比分）
     */
    void finishChallenge(Long challengeId, Long userId, Integer scoreInitiator,
                         Integer scoreOpponent, Long winnerId);

    /**
     * 获取我的约战列表
     */
    Page<Challenge> getMyChallenges(Long userId, Integer type, Integer page, Integer size);
}













