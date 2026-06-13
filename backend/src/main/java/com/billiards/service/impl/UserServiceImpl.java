











package com.billiards.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billiards.common.BusinessException;
import com.billiards.common.Constants;
import com.billiards.entity.User;
import com.billiards.mapper.UserMapper;
import com.billiards.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String code) {
        // 实际项目中应通过 code 调用微信接口获取 openid
        // 此处简化处理，用 code 模拟 openid（生产环境需替换）
        String openid = "mock_openid_" + code;

        User user = lambdaQuery().eq(User::getOpenid, openid).one();
        if (user == null) {
            // 新用户自动注册
            user = new User();
            user.setOpenid(openid);
            user.setNickname("球友" + System.currentTimeMillis() % 10000);
            user.setLevelScore(Constants.DEFAULT_LEVEL_SCORE);
            user.setCreditScore(Constants.DEFAULT_CREDIT_SCORE);
            user.setStatus(1);
            save(user);
            log.info("新用户注册: openid={}", openid);
        }
        return user;
    }

    @Override
    public void updateProfile(Long userId, String nickname, String avatarUrl, Integer gender) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setNickname(nickname);
        user.setAvatarUrl(avatarUrl);
        user.setGender(gender);
        updateById(user);
    }
}











