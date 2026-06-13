



package com.billiards.common;

/**
 * 系统常量
 */
public interface Constants {

    /** 用户相关 */
    String USER_STATUS_NORMAL = "1";
    String USER_STATUS_DISABLED = "0";

    /** 约战状态 */
    int CHALLENGE_PENDING = 0;       // 待应战
    int CHALLENGE_IN_PROGRESS = 1;   // 进行中
    int CHALLENGE_FINISHED = 2;      // 已完成
    int CHALLENGE_CANCELLED = 3;     // 已取消

    /** 报名状态 */
    int SIGNUP_PENDING = 0;   // 待确认
    int SIGNUP_CONFIRMED = 1; // 已确认
    int SIGNUP_REJECTED = 2;  // 已拒绝

    /** 球种 */
    int BALL_TYPE_8 = 1;  // 中式八球
    int BALL_TYPE_SNOOKER = 2; // 斯诺克
    int BALL_TYPE_9 = 3;  // 九球

    /** 赛制 */
    int FORMAT_RACE = 1;   // 抢X
    int FORMAT_TIMED = 2;  // 限时

    /** 默认段位积分 */
    int DEFAULT_LEVEL_SCORE = 1000;
    int DEFAULT_CREDIT_SCORE = 100;
}




