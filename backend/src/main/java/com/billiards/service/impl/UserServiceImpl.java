











package com.billiards.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billiards.common.BusinessException;
import com.billiards.common.Constants;
import com.billiards.common.TokenManager;
import com.billiards.dto.UserLoginVO;
import com.billiards.dto.UserStatsVO;
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
    public UserLoginVO login(String code) {
        // 实际项目中应通过 code 调用微信接口获取 openid
        // 此处简化处理，用 code 模拟 openid（生产环境需替换）
        String openid = "mock_openid_" + code;

        User user = lambdaQuery().eq(User::getOpenid, openid).one();
        if (user == null) {
            // 新用户自动注册
            user = new User();
            user.setOpenid(openid);
            user.setNickname("球友" + System.currentTimeMillis() % 10000);
            user.setLevelScore(0);
            user.setWins(0);
            user.setLosses(0);
            user.setCreditScore(Constants.DEFAULT_CREDIT_SCORE);
            user.setStatus(1);
            save(user);
            log.info("新用户注册: openid={}", openid);
        }

        // 生成 token
        String token = TokenManager.generateToken(user.getId());

        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getId());
        vo.setToken(token);
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setLevelScore(user.getLevelScore());
        vo.setWins(user.getWins());
        vo.setLosses(user.getLosses());
        vo.setCreditScore(user.getCreditScore());
        return vo;
    }

    @Override
    public User getCurrentUser(String token) {
        if (!TokenManager.isValid(token)) {
            throw new BusinessException(401, "token无效或已过期");
        }
        Long userId = TokenManager.getUserIdByToken(token);
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
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

    @Override
    public UserStatsVO getUserStats(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserStatsVO vo = new UserStatsVO();
        vo.setUserId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setLevelScore(user.getLevelScore());
        vo.setLevelName(UserStatsVO.getLevelName(user.getLevelScore()));
        vo.setWins(user.getWins());
        vo.setLosses(user.getLosses());
        vo.setTotalGames(user.getWins() + user.getLosses());
        vo.setWinRate(user.getWins() + user.getLosses() > 0
                ? (double) Math.round((double) user.getWins() / (user.getWins() + user.getLosses()) * 10000) / 100
                : 0.0);
        vo.setCreditScore(user.getCreditScore());
        return vo;
    }
}











