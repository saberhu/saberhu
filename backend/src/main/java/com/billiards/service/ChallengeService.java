











package com.billiards.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.billiards.dto.ChallengeDetailVO;
import com.billiards.entity.Challenge;

import java.time.LocalDateTime;

/**
 * 约战服务接口
 */
public interface ChallengeService extends IService<Challenge> {

    /**
     * 发起约战
     */
    Challenge createChallenge(Long userId, Long ballroomId, Integer ballType,
                              Integer formatType, Integer formatValue,
                              LocalDateTime startTime,
                              Integer maxPlayers, String remark);

    /**
     * 约战列表：支持按球种筛选，按时间排序，返回发起人信息和球房信息
     */
    Page<Challenge> getChallengePage(Integer page, Integer size, Integer ballType, Integer status);

    /**
     * 约战详情（含发起人、球房、报名信息）
     */
    ChallengeDetailVO getChallengeDetail(Long challengeId);

    /**
     * 取消约战（仅发起人）
     */
    void cancelChallenge(Long userId, Long challengeId);

    /**
     * 结束约战提交比分：自动更新双方战绩和段位积分
     */
    void finishChallenge(Long challengeId, Long userId, Integer scoreInitiator,
                         Integer scoreOpponent, Long winnerId);

    /**
     * 我的约战列表：区分我发起的、我参加的、历史的
     */
    Page<Challenge> getMyChallenges(Long userId, Integer type, Integer page, Integer size);
}











