








package com.billiards.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billiards.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 微信登录，用户不存在则自动注册
     */
    User login(String code);

    /**
     * 更新用户资料
     */
    void updateProfile(Long userId, String nickname, String avatarUrl, Integer gender);
}








