








package com.billiards.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.billiards.dto.BallroomPageDTO;
import com.billiards.entity.Ballroom;

import java.util.List;

/**
 * 球房服务接口
 */
public interface BallroomService extends IService<Ballroom> {

    /**
     * 球房列表：支持按距离排序、按评分排序
     */
    Page<Ballroom> getBallroomPage(BallroomPageDTO dto);

    /**
     * 球房详情
     */
    Ballroom getBallroomDetail(Long id);

    /**
     * 更新球房评分（新增评价时调用）
     */
    void updateRating(Long ballroomId);

    /**
     * 收藏/取消收藏球房
     */
    boolean toggleFavorite(Long userId, Long ballroomId);

    /**
     * 是否已收藏
     */
    boolean checkFavorite(Long userId, Long ballroomId);

    /**
     * 我的收藏列表
     */
    List<Ballroom> getMyFavorites(Long userId);
}








