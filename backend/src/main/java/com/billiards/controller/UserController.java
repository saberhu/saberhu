













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
     * 请求参数：{ "code": "微信临时code" }
     * 返回：{ "userId": 1, "token": "xxx", "nickname": "球友xxx", ... }
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        UserLoginVO vo = userService.login(dto.getCode());
        return Result.success(vo);
    }

    /**
     * 获取当前用户信息（根据 token）
     * GET /api/user/current?token=xxx
     */
    @GetMapping("/current")
    public Result<User> getCurrentUser(@RequestParam String token) {
        User user = userService.getCurrentUser(token);
        return Result.success(user);
    }

    /**
     * 获取用户信息
     * GET /api/user/{id}
     */
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.notFound("用户不存在");
        }
        return Result.success(user);
    }

    /**
     * 更新用户资料
     * PUT /api/user/{id}/profile
     * 请求参数：{ "nickname": "xxx", "avatarUrl": "xxx", "gender": 1 }
     */
    @PutMapping("/{id}/profile")
    public Result<Void> updateProfile(@PathVariable Long id,
                                       @Valid @RequestBody UserProfileUpdateDTO dto) {
        userService.updateProfile(id, dto.getNickname(), dto.getAvatarUrl(), dto.getGender());
        return Result.success();
    }

    /**
     * 获取我的战绩统计
     * GET /api/user/{id}/stats
     * 返回：{ "levelScore": 100, "levelName": "白银", "wins": 5, "losses": 3, "winRate": 62.5, ... }
     */
    @GetMapping("/{id}/stats")
    public Result<UserStatsVO> getUserStats(@PathVariable Long id) {
        UserStatsVO stats = userService.getUserStats(id);
        return Result.success(stats);
    }
}













