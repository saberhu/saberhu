








package com.billiards.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billiards.dto.UserLoginVO;
import com.billiards.dto.UserStatsVO;
import com.billiards.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 微信登录：接收 code，模拟换取 openid，自动注册/更新用户信息，返回 token
     */
    UserLoginVO login(String code);

    /**
     * 根据 token 获取当前用户信息
     */
    User getCurrentUser(String token);

    /**
     * 更新用户资料（昵称、头像）
     */
    void updateProfile(Long userId, String nickname, String avatarUrl, Integer gender);

    /**
     * 获取我的战绩统计
     */
    UserStatsVO getUserStats(Long userId);
}








