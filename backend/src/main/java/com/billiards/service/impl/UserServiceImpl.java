








package com.billiards.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.billiards.common.BusinessException;
import com.billiards.common.Constants;
import com.billiards.common.TokenManager;
import com.billiards.dto.UserLoginVO;
import com.billiards.dto.UserStatsVO;
import com.billiards.entity.Challenge;
import com.billiards.entity.User;
import com.billiards.mapper.ChallengeMapper;
import com.billiards.mapper.UserMapper;
import com.billiards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final ChallengeMapper challengeMapper;
    private final TokenManager tokenManager;

    @Override
    public UserLoginVO login(String code, String nickname, String avatarUrl) {
        // 本地开发：用 code 模拟 openid
        String openid = "mock_openid_" + code;

        // 查找是否已注册
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));

        if (user == null) {
            // 新用户注册
            user = new User();
            user.setOpenid(openid);
            user.setNickname(nickname != null ? nickname : "球友" + System.currentTimeMillis() % 10000);
            user.setAvatarUrl(avatarUrl != null ? avatarUrl : "");
            user.setLevelScore(Constants.DEFAULT_LEVEL_SCORE);
            user.setWins(0);
            user.setLosses(0);
            user.setCreditScore(Constants.DEFAULT_CREDIT_SCORE);
            user.setStatus(Constants.USER_STATUS_NORMAL);
            userMapper.insert(user);
        } else if (nickname != null && !nickname.equals(user.getNickname())) {
            // 更新昵称和头像
            user.setNickname(nickname);
            user.setAvatarUrl(avatarUrl);
            userMapper.updateById(user);
        }

        // 生成 token
        String token = tokenManager.generateToken(user.getId());

        UserLoginVO vo = new UserLoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setLevelScore(user.getLevelScore());
        return vo;
    }

    @Override
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, String nickname, String avatarUrl, String phone, Integer gender) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        if (nickname != null) user.setNickname(nickname);
        if (avatarUrl != null) user.setAvatarUrl(avatarUrl);
        if (phone != null) user.setPhone(phone);
        if (gender != null) user.setGender(gender);
        userMapper.updateById(user);
    }

    @Override
    public UserStatsVO getUserStats(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        UserStatsVO vo = new UserStatsVO();
        vo.setLevelScore(user.getLevelScore());
        vo.setWins(user.getWins());
        vo.setLosses(user.getLosses());
        vo.setTotalGames(user.getWins() + user.getLosses());
        vo.setWinRate(user.getWins() + user.getLosses() > 0
                ? String.format("%.1f%%", user.getWins() * 100.0 / (user.getWins() + user.getLosses()))
                : "0%");
        vo.setCreditScore(user.getCreditScore());

        // 统计约战总数
        Long totalChallenges = challengeMapper.selectCount(
                new LambdaQueryWrapper<Challenge>()
                        .eq(Challenge::getInitiatorId, userId));
        vo.setTotalChallenges(totalChallenges.intValue());

        vo.setFavoriteCount(0); // 由前端或单独接口统计
        return vo;
    }
}








