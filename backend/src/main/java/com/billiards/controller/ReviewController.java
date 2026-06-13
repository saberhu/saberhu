
















package com.billiards.controller;

import com.billiards.common.Result;
import com.billiards.dto.BallroomReviewDTO;
import com.billiards.entity.BallroomReview;
import com.billiards.mapper.BallroomReviewMapper;
import com.billiards.service.BallroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 评价控制器
 */
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final BallroomReviewMapper reviewMapper;
    private final BallroomService ballroomService;

    /**
     * 提交评价
     */
    @PostMapping("/submit")
    public Result<Void> submit(@RequestParam Long userId,
                                @Valid @RequestBody BallroomReviewDTO dto) {
        BallroomReview review = new BallroomReview();
        review.setBallroomId(dto.getBallroomId());
        review.setUserId(userId);
        review.setRating(BigDecimal.valueOf(dto.getRating()));
        review.setContent(dto.getContent() != null ? dto.getContent() : "");
        review.setImages(dto.getImages());
        reviewMapper.insert(review);

        // 更新球房评分
        ballroomService.updateRating(dto.getBallroomId());
        return Result.success();
    }
}
















