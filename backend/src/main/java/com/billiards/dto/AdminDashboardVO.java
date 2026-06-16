

package com.billiards.dto;

import lombok.Data;

/**
 * 管理员首页统计数据
 */
@Data
public class AdminDashboardVO {

    /** 用户总数 */
    private Long totalUsers;
    /** 今日新增用户 */
    private Long todayNewUsers;
    /** 球房总数 */
    private Long totalBallrooms;
    /** 营业中球房数 */
    private Long activeBallrooms;
    /** 约战总数 */
    private Long totalChallenges;
    /** 进行中约战 */
    private Long ongoingChallenges;
    /** 评价总数 */
    private Long totalReviews;
}

