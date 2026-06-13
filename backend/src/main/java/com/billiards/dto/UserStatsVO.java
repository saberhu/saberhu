













package com.billiards.dto;

import lombok.Data;

/**
 * 用户统计数据
 */
@Data
public class UserStatsVO {

    private Integer levelScore;
    private Integer wins;
    private Integer losses;
    private Integer totalGames;
    private String winRate;
    private Integer creditScore;
    private Integer totalChallenges;
    private Integer favoriteCount;
}













