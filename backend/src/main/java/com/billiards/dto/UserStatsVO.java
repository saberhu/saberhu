



package com.billiards.dto;

import lombok.Data;

/**
 * 用户战绩统计 VO
 */
@Data
public class UserStatsVO {

    private Long userId;
    private String nickname;
    private String avatarUrl;
    private Integer levelScore;
    private String levelName;
    private Integer wins;
    private Integer losses;
    private Integer totalGames;
    private Double winRate;
    private Integer creditScore;

    /**
     * 根据段位积分获取段位名称
     */
    public static String getLevelName(int score) {
        if (score >= 600) return "铂金";
        if (score >= 300) return "黄金";
        if (score >= 100) return "白银";
        return "青铜";
    }
}



