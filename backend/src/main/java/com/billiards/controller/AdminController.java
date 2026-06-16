


package com.billiards.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.billiards.common.Result;
import com.billiards.dto.*;
import com.billiards.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 管理员登录
     * POST /api/admin/login
     */
    @PostMapping("/login")
    public Result<AdminLoginVO> login(@Valid @RequestBody AdminLoginDTO dto) {
        return Result.success(adminService.login(dto.getUsername(), dto.getPassword()));
    }

    /**
     * 获取首页统计数据
     * GET /api/admin/dashboard
     */
    @GetMapping("/dashboard")
    public Result<AdminDashboardVO> dashboard() {
        return Result.success(adminService.getDashboard());
    }

    /**
     * 分页查询用户列表
     * GET /api/admin/users?page=1&size=10&keyword=
     */
    @GetMapping("/users")
    public Result<IPage<AdminUserPageVO>> pageUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(adminService.pageUsers(page, size, keyword));
    }

    /**
     * 启用/禁用用户
     * PUT /api/admin/users/{id}/status
     */
    @PutMapping("/users/{id}/status")
    public Result<Void> toggleUserStatus(@PathVariable Long id,
                                          @RequestParam Integer status) {
        adminService.toggleUserStatus(id, status);
        return Result.success();
    }

    /**
     * 分页查询球房列表
     * GET /api/admin/ballrooms?page=1&size=10&keyword=
     */
    @GetMapping("/ballrooms")
    public Result<IPage<AdminBallroomPageVO>> pageBallrooms(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(adminService.pageBallrooms(page, size, keyword));
    }

    /**
     * 启用/禁用球房
     * PUT /api/admin/ballrooms/{id}/status
     */
    @PutMapping("/ballrooms/{id}/status")
    public Result<Void> toggleBallroomStatus(@PathVariable Long id,
                                              @RequestParam Integer status) {
        adminService.toggleBallroomStatus(id, status);
        return Result.success();
    }
}


