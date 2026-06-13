





package com.billiards.controller;

import com.billiards.common.Result;
import com.billiards.dto.UserLoginDTO;
import com.billiards.dto.UserLoginVO;
import com.billiards.dto.UserProfileUpdateDTO;
import com.billiards.dto.UserStatsVO;
import com.billiards.entity.User;
import com.billiards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 微信登录
     * POST /api/user/login
     * { "code": "xxx", "nickname": "昵称", "avatarUrl": "头像URL" }
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        UserLoginVO vo = userService.login(dto.getCode(), dto.getNickname(), dto.getAvatarUrl());
        return Result.success(vo);
    }

    /**
     * 获取用户信息
     * GET /api/user/{id}
     */
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    /**
     * 更新用户资料
     * PUT /api/user/{id}/profile
     */
    @PutMapping("/{id}/profile")
    public Result<Void> updateProfile(@PathVariable Long id,
                                       @RequestBody UserProfileUpdateDTO dto) {
        userService.updateProfile(id, dto.getNickname(), dto.getAvatarUrl(),
                dto.getPhone(), dto.getGender());
        return Result.success();
    }

    /**
     * 获取用户统计数据
     * GET /api/user/{id}/stats
     */
    @GetMapping("/{id}/stats")
    public Result<UserStatsVO> getStats(@PathVariable Long id) {
        return Result.success(userService.getUserStats(id));
    }
}





