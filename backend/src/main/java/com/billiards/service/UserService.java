





package com.billiards.service;

import com.billiards.dto.UserLoginVO;
import com.billiards.dto.UserStatsVO;
import com.billiards.entity.User;

/**
 * 用户业务接口
 */
public interface UserService {

    /**
     * 微信登录（本地开发直接注册/登录）
     */
    UserLoginVO login(String code, String nickname, String avatarUrl);

    /**
     * 获取用户信息
     */
    User getUserById(Long userId);

    /**
     * 更新用户资料
     */
    void updateProfile(Long userId, String nickname, String avatarUrl, String phone, Integer gender);

    /**
     * 获取用户统计数据
     */
    UserStatsVO getUserStats(Long userId);
}





