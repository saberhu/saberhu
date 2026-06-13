
















package com.billiards.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billiards.common.Result;
import com.billiards.dto.BallroomPageDTO;
import com.billiards.dto.BallroomReviewDTO;
import com.billiards.entity.Ballroom;
import com.billiards.entity.BallroomReview;
import com.billiards.mapper.BallroomReviewMapper;
import com.billiards.service.BallroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 球房控制器
 */
@RestController
@RequestMapping("/api/ballroom")
@RequiredArgsConstructor
public class BallroomController {

    private final BallroomService ballroomService;
    private final BallroomReviewMapper reviewMapper;

    /**
     * 球房列表：支持按距离排序、按评分排序
     * GET /api/ballroom/page?page=1&size=10&sortBy=rating&latitude=32.89&longitude=115.82
     */
    @GetMapping("/page")
    public Result<Page<Ballroom>> page(BallroomPageDTO dto) {
        Page<Ballroom> result = ballroomService.getBallroomPage(dto);
        return Result.success(result);
    }

    /**
     * 球房详情
     * GET /api/ballroom/{id}
     */
    @GetMapping("/{id}")
    public Result<Ballroom> detail(@PathVariable Long id) {
        Ballroom ballroom = ballroomService.getBallroomDetail(id);
        return Result.success(ballroom);
    }

    /**
     * 获取球房评价列表
     * GET /api/ballroom/{id}/reviews?page=1&size=10
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
     * 发布球房评价
     * POST /api/ballroom/{id}/review?userId=1
     * 请求参数：{ "rating": 4.5, "content": "环境不错", "images": "" }
     */
    @PostMapping("/{id}/review")
    public Result<Void> submitReview(@PathVariable Long id,
                                      @RequestParam Long userId,
                                      @Valid @RequestBody BallroomReviewDTO dto) {
        BallroomReview review = new BallroomReview();
        review.setBallroomId(id);
        review.setUserId(userId);
        review.setRating(BigDecimal.valueOf(dto.getRating()));
        review.setContent(dto.getContent() != null ? dto.getContent() : "");
        review.setImages(dto.getImages());
        reviewMapper.insert(review);

        // 更新球房评分
        ballroomService.updateRating(id);
        return Result.success();
    }

    /**
     * 收藏/取消收藏球房
     * POST /api/ballroom/{id}/favorite?userId=1
     */
    @PostMapping("/{id}/favorite")
    public Result<Boolean> toggleFavorite(@PathVariable Long id, @RequestParam Long userId) {
        boolean isFavorite = ballroomService.toggleFavorite(userId, id);
        return Result.success(isFavorite);
    }

    /**
     * 是否已收藏
     * GET /api/ballroom/{id}/favorite/check?userId=1
     */
    @GetMapping("/{id}/favorite/check")
    public Result<Boolean> checkFavorite(@PathVariable Long id, @RequestParam Long userId) {
        boolean isFavorite = ballroomService.checkFavorite(userId, id);
        return Result.success(isFavorite);
    }

    /**
     * 我的收藏列表
     * GET /api/ballroom/favorite/list?userId=1
     */
    @GetMapping("/favorite/list")
    public Result<List<Ballroom>> myFavorites(@RequestParam Long userId) {
        List<Ballroom> list = ballroomService.getMyFavorites(userId);
        return Result.success(list);
    }
}
















