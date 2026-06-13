









package com.billiards.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billiards.dto.BallroomPageDTO;
import com.billiards.entity.Ballroom;
import com.billiards.entity.BallroomReview;

import java.math.BigDecimal;
import java.util.List;

/**
 * 球房业务接口
 */
public interface BallroomService {

    /**
     * 球房分页列表
     */
    Page<BallroomPageDTO> getBallroomPage(Integer page, Integer size, String keyword, Long userId);

    /**
     * 球房详情
     */
    Ballroom getBallroomById(Long id);

    /**
     * 获取球房评价列表
     */
    Page<BallroomReview> getReviews(Long ballroomId, Integer page, Integer size);

    /**
     * 提交评价
     */
    void submitReview(Long userId, Long ballroomId, BigDecimal rating, String content, String images);

    /**
     * 收藏/取消收藏
     */
    boolean toggleFavorite(Long userId, Long ballroomId);

    /**
     * 检查是否已收藏
     */
    boolean checkFavorite(Long userId, Long ballroomId);

    /**
     * 获取用户收藏的球房列表
     */
    List<Ballroom> getUserFavorites(Long userId);

    /**
     * 获取热门球房推荐
     */
    List<Ballroom> getHotBallrooms(int limit);
}








