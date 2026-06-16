

package com.billiards.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billiards.common.BusinessException;
import com.billiards.common.TokenManager;
import com.billiards.dto.*;
import com.billiards.entity.Admin;
import com.billiards.entity.Ballroom;
import com.billiards.entity.Challenge;
import com.billiards.entity.User;
import com.billiards.mapper.*;
import com.billiards.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 管理员业务实现
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final BallroomMapper ballroomMapper;
    private final ChallengeMapper challengeMapper;
    private final BallroomReviewMapper ballroomReviewMapper;
    private final TokenManager tokenManager;

    @Override
    public AdminLoginVO login(String username, String password) {
        Admin admin = adminMapper.selectOne(
                new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));

        if (admin == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (admin.getStatus() == 0) {
            throw new BusinessException(401, "账号已被禁用");
        }

        String md5Pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Pwd.equals(admin.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        String token = tokenManager.generateToken(admin.getId());

        AdminLoginVO vo = new AdminLoginVO();
        vo.setAdminId(admin.getId());
        vo.setToken(token);
        vo.setNickname(admin.getNickname());
        vo.setRole(admin.getRole());
        return vo;
    }

    @Override
    public AdminDashboardVO getDashboard() {
        AdminDashboardVO vo = new AdminDashboardVO();

        // 用户统计
        vo.setTotalUsers(userMapper.selectCount(null));
        vo.setTodayNewUsers(userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                        .ge(User::getCreateTime, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))));

        // 球房统计
        vo.setTotalBallrooms(ballroomMapper.selectCount(null));
        vo.setActiveBallrooms(ballroomMapper.selectCount(
                new LambdaQueryWrapper<Ballroom>().eq(Ballroom::getStatus, 1)));

        // 约战统计
        vo.setTotalChallenges(challengeMapper.selectCount(null));
        vo.setOngoingChallenges(challengeMapper.selectCount(
                new LambdaQueryWrapper<Challenge>().eq(Challenge::getStatus, 1)));

        // 评价统计
        vo.setTotalReviews(ballroomReviewMapper.selectCount(null));

        return vo;
    }

    @Override
    public IPage<AdminUserPageVO> pageUsers(Integer page, Integer size, String keyword) {
        Page<User> userPage = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getNickname, keyword)
                   .or().like(User::getPhone, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);

        IPage<User> p = userMapper.selectPage(userPage, wrapper);

        // 转换为 VO，带上约战数
        IPage<AdminUserPageVO> voPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        voPage.setRecords(p.getRecords().stream().map(u -> {
            AdminUserPageVO vo = new AdminUserPageVO();
            vo.setId(u.getId());
            vo.setNickname(u.getNickname());
            vo.setPhone(u.getPhone());
            vo.setGender(u.getGender());
            vo.setLevelScore(u.getLevelScore());
            vo.setWins(u.getWins());
            vo.setLosses(u.getLosses());
            vo.setCreditScore(u.getCreditScore());
            vo.setStatus(u.getStatus());
            vo.setCreateTime(u.getCreateTime());

            Long cnt = challengeMapper.selectCount(
                    new LambdaQueryWrapper<Challenge>()
                            .eq(Challenge::getInitiatorId, u.getId()));
            vo.setTotalChallenges(cnt.intValue());
            return vo;
        }).toList());

        return voPage;
    }

    @Override
    @Transactional
    public void toggleUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setStatus(status);
        userMapper.updateById(user);
    }

    @Override
    public IPage<AdminBallroomPageVO> pageBallrooms(Integer page, Integer size, String keyword) {
        Page<Ballroom> brPage = new Page<>(page, size);
        LambdaQueryWrapper<Ballroom> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Ballroom::getName, keyword)
                   .or().like(Ballroom::getAddress, keyword);
        }
        wrapper.orderByDesc(Ballroom::getCreateTime);

        IPage<Ballroom> p = ballroomMapper.selectPage(brPage, wrapper);

        IPage<AdminBallroomPageVO> voPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        voPage.setRecords(p.getRecords().stream().map(b -> {
            AdminBallroomPageVO vo = new AdminBallroomPageVO();
            vo.setId(b.getId());
            vo.setName(b.getName());
            vo.setAddress(b.getAddress());
            vo.setPhone(b.getPhone());
            vo.setPriceDesc(b.getPriceDesc());
            vo.setBusinessHours(b.getBusinessHours());
            vo.setRating(b.getRating());
            vo.setRatingCount(b.getRatingCount());
            vo.setStatus(b.getStatus());
            vo.setCreateTime(b.getCreateTime());

            Long cnt = challengeMapper.selectCount(
                    new LambdaQueryWrapper<Challenge>()
                            .eq(Challenge::getBallroomId, b.getId()));
            vo.setTotalChallenges(cnt.intValue());
            return vo;
        }).toList());

        return voPage;
    }

    @Override
    @Transactional
    public void toggleBallroomStatus(Long ballroomId, Integer status) {
        Ballroom ballroom = ballroomMapper.selectById(ballroomId);
        if (ballroom == null) {
            throw new BusinessException(404, "球房不存在");
        }
        ballroom.setStatus(status);
        ballroomMapper.updateById(ballroom);
    }
}

