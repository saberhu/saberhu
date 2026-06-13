













package com.billiards.controller;

import com.billiards.common.Result;
import com.billiards.dto.UserLoginDTO;
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
     */
    @PostMapping("/login")
    public Result<User> login(@Valid @RequestBody UserLoginDTO dto) {
        User user = userService.login(dto.getCode());
        return Result.success(user);
    }

    /**
     * 获取用户信息
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
     */
    @PutMapping("/{id}/profile")
    public Result<Void> updateProfile(@PathVariable Long id,
                                       @RequestParam String nickname,
                                       @RequestParam String avatarUrl,
                                       @RequestParam Integer gender) {
        userService.updateProfile(id, nickname, avatarUrl, gender);
        return Result.success();
    }
}













