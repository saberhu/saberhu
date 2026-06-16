

package com.billiards.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.billiards.dto.*;

/**
 * 管理员业务接口
 */
public interface AdminService {

    /**
     * 管理员登录
     */
    AdminLoginVO login(String username, String password);

    /**
     * 获取首页统计数据
     */
    AdminDashboardVO getDashboard();

    /**
     * 分页查询用户列表
     */
    IPage<AdminUserPageVO> pageUsers(Integer page, Integer size, String keyword);

    /**
     * 启用/禁用用户
     */
    void toggleUserStatus(Long userId, Integer status);

    /**
     * 分页查询球房列表
     */
    IPage<AdminBallroomPageVO> pageBallrooms(Integer page, Integer size, String keyword);

    /**
     * 启用/禁用球房
     */
    void toggleBallroomStatus(Long ballroomId, Integer status);
}

