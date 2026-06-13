













package com.billiards.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billiards.entity.Ballroom;
import com.billiards.entity.BallroomReview;
import com.billiards.mapper.BallroomMapper;
import com.billiards.mapper.BallroomReviewMapper;
import com.billiards.service.BallroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 球房服务实现
 */
@Service
@RequiredArgsConstructor
public class BallroomServiceImpl extends ServiceImpl<BallroomMapper, Ballroom> implements BallroomService {

    private final BallroomReviewMapper reviewMapper;

    @Override
    public void updateRating(Long ballroomId) {
        // 计算平均分
        Double avg = reviewMapper.selectList(null).stream()
                .filter(r -> r.getBallroomId().equals(ballroomId))
                .mapToDouble(r -> r.getRating().doubleValue())
                .average()
                .orElse(0.0);

        long count = reviewMapper.selectList(null).stream()
                .filter(r -> r.getBallroomId().equals(ballroomId))
                .count();

        Ballroom ballroom = getById(ballroomId);
        if (ballroom != null) {
            ballroom.setRating(BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP));
            ballroom.setRatingCount((int) count);
            updateById(ballroom);
        }
    }
}













