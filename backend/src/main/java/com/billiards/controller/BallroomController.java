











package com.billiards.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billiards.common.Result;
import com.billiards.dto.BallroomPageDTO;
import com.billiards.dto.BallroomReviewDTO;
import com.billiards.entity.Ballroom;
import com.billiards.entity.BallroomReview;
import com.billiards.service.BallroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 球房控制器
 */
@RestController
@RequestMapping("/api/ballroom")
@RequiredArgsConstructor
public class BallroomController {

    private final BallroomService ballroomService;

    /**
     * 球房分页列表
     * GET /api/ballroom/page?page=1&size=10&keyword=xxx&userId=1
     */
    @GetMapping("/page")
    public Result<Page<BallroomPageDTO>> page(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Long userId) {
        return Result.success(ballroomService.getBallroomPage(page, size, keyword, userId));
    }

    /**
     * 球房详情
     * GET /api/ballroom/{id}
     */
    @GetMapping("/{id}")
    public Result<Ballroom> detail(@PathVariable Long id) {
        return Result.success(ballroomService.getBallroomById(id));
    }

    /**
     * 球房评价列表
     * GET /api/ballroom/{id}/reviews?page=1&size=10
     */
    @GetMapping("/{id}/reviews")
    public Result<Page<BallroomReview>> reviews(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "1") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(ballroomService.getReviews(id, page, size));
    }

    /**
     * 提交评价
     * POST /api/ballroom/review
     */
    @PostMapping("/review")
    public Result<Void> submitReview(@RequestHeader("userId") Long userId,
                                      @Valid @RequestBody BallroomReviewDTO dto) {
        ballroomService.submitReview(userId, dto.getBallroomId(), dto.getRating(),
                dto.getContent(), dto.getImages());
        return Result.success();
    }

    /**
     * 收藏/取消收藏
     * POST /api/ballroom/{id}/favorite
     */
    @PostMapping("/{id}/favorite")
    public Result<Boolean> toggleFavorite(@RequestHeader("userId") Long userId,
                                           @PathVariable Long id) {
        return Result.success(ballroomService.toggleFavorite(userId, id));
    }

    /**
     * 检查是否已收藏
     * GET /api/ballroom/{id}/favorite/check?userId=1
     */
    @GetMapping("/{id}/favorite/check")
    public Result<Boolean> checkFavorite(@RequestParam Long userId,
                                          @PathVariable Long id) {
        return Result.success(ballroomService.checkFavorite(userId, id));
    }

    /**
     * 用户收藏列表
     * GET /api/ballroom/favorites/{userId}
     */
    @GetMapping("/favorites/{userId}")
    public Result<List<Ballroom>> favorites(@PathVariable Long userId) {
        return Result.success(ballroomService.getUserFavorites(userId));
    }

    /**
     * 用户收藏列表（前端兼容）
     * GET /api/ballroom/favorite/list?userId=3
     */
    @GetMapping("/favorite/list")
    public Result<List<Ballroom>> favoriteList(@RequestParam Long userId) {
        return Result.success(ballroomService.getUserFavorites(userId));
    }

    /**
     * 热门球房推荐
     * GET /api/ballroom/hot?limit=5
     */
    @GetMapping("/hot")
    public Result<List<Ballroom>> hot(@RequestParam(defaultValue = "5") Integer limit) {
        return Result.success(ballroomService.getHotBallrooms(limit));
    }
}











