




package com.billiards.common;

/**
 * 系统常量
 */
public interface Constants {

    /** 用户状态 */
    int USER_STATUS_NORMAL = 1;
    int USER_STATUS_DISABLED = 0;

    /** 球种：1-中式八球 2-斯诺克 3-九球 */
    int BALL_TYPE_EIGHT = 1;
    int BALL_TYPE_SNOOKER = 2;
    int BALL_TYPE_NINE = 3;

    /** 赛制：1-抢X 2-限时 */
    int FORMAT_RACE = 1;
    int FORMAT_TIMED = 2;

    /** 约战状态：0-待应战 1-进行中 2-已完成 3-已取消 */
    int CHALLENGE_PENDING = 0;
    int CHALLENGE_ONGOING = 1;
    int CHALLENGE_FINISHED = 2;
    int CHALLENGE_CANCELED = 3;

    /** 报名状态：0-待确认 1-已确认 2-已拒绝 */
    int SIGNUP_PENDING = 0;
    int SIGNUP_CONFIRMED = 1;
    int SIGNUP_REJECTED = 2;

    /** 球房状态 */
    int BALLROOM_OPEN = 1;
    int BALLROOM_CLOSED = 0;

    /** 默认段位积分 */
    int DEFAULT_LEVEL_SCORE = 1000;
    /** 默认信用分 */
    int DEFAULT_CREDIT_SCORE = 100;
}




