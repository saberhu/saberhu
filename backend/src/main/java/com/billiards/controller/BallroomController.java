
















package com.billiards.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billiards.common.Result;
import com.billiards.entity.Ballroom;
import com.billiards.entity.BallroomFavorite;
import com.billiards.entity.BallroomReview;
import com.billiards.mapper.BallroomFavoriteMapper;
import com.billiards.mapper.BallroomReviewMapper;
import com.billiards.service.BallroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 球房控制器
 */
@RestController
@RequestMapping("/api/ballroom")
@RequiredArgsConstructor
public class BallroomController {

    private final BallroomService ballroomService;
    private final BallroomReviewMapper reviewMapper;
    private final BallroomFavoriteMapper favoriteMapper;

    /**
     * 球房列表（分页）
     */
    @GetMapping("/page")
    public Result<Page<Ballroom>> page(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        Page<Ballroom> result = ballroomService.page(new Page<>(page, size),
                new LambdaQueryWrapper<Ballroom>().eq(Ballroom::getStatus, 1));
        return Result.success(result);
    }

    /**
     * 球房详情
     */
    @GetMapping("/{id}")
    public Result<Ballroom> detail(@PathVariable Long id) {
        Ballroom ballroom = ballroomService.getById(id);
        if (ballroom == null) {
            return Result.notFound("球房不存在");
        }
        return Result.success(ballroom);
    }

    /**
     * 获取球房评价列表
     */
    @GetMapping("/{id}/reviews")
    public Result<Page<BallroomReview>> reviews(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "1") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        Page<BallroomReview> result = reviewMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<BallroomReview>()
                        .eq(BallroomReview::getBallroomId, id)
                        .orderByDesc(BallroomReview::getCreateTime));
        return Result.success(result);
    }

    /**
     * 收藏/取消收藏球房
     */
    @PostMapping("/favorite")
    public Result<Void> toggleFavorite(@RequestParam Long userId, @RequestParam Long ballroomId) {
        BallroomFavorite existing = favoriteMapper.selectOne(
                new LambdaQueryWrapper<BallroomFavorite>()
                        .eq(BallroomFavorite::getUserId, userId)
                        .eq(BallroomFavorite::getBallroomId, ballroomId));
        if (existing != null) {
            favoriteMapper.deleteById(existing.getId());
        } else {
            BallroomFavorite fav = new BallroomFavorite();
            fav.setUserId(userId);
            fav.setBallroomId(ballroomId);
            favoriteMapper.insert(fav);
        }
        return Result.success();
    }

    /**
     * 是否已收藏
     */
    @GetMapping("/favorite/check")
    public Result<Boolean> checkFavorite(@RequestParam Long userId, @RequestParam Long ballroomId) {
        Long count = favoriteMapper.selectCount(
                new LambdaQueryWrapper<BallroomFavorite>()
                        .eq(BallroomFavorite::getUserId, userId)
                        .eq(BallroomFavorite::getBallroomId, ballroomId));
        return Result.success(count > 0);
    }
}
















