













package com.billiards.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billiards.common.BusinessException;
import com.billiards.dto.BallroomPageDTO;
import com.billiards.entity.Ballroom;
import com.billiards.entity.BallroomFavorite;
import com.billiards.entity.BallroomReview;
import com.billiards.mapper.BallroomFavoriteMapper;
import com.billiards.mapper.BallroomMapper;
import com.billiards.mapper.BallroomReviewMapper;
import com.billiards.service.BallroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 球房服务实现
 */
@Service
@RequiredArgsConstructor
public class BallroomServiceImpl extends ServiceImpl<BallroomMapper, Ballroom> implements BallroomService {

    private final BallroomReviewMapper reviewMapper;
    private final BallroomFavoriteMapper favoriteMapper;

    @Override
    public Page<Ballroom> getBallroomPage(BallroomPageDTO dto) {
        LambdaQueryWrapper<Ballroom> wrapper = new LambdaQueryWrapper<Ballroom>()
                .eq(Ballroom::getStatus, 1);

        // 按评分排序
        if ("rating".equals(dto.getSortBy())) {
            wrapper.orderByDesc(Ballroom::getRating);
        } else {
            // 默认按创建时间排序
            wrapper.orderByDesc(Ballroom::getCreateTime);
        }

        Page<Ballroom> page = page(new Page<>(dto.getPage(), dto.getSize()), wrapper);

        // 按距离排序（如果有传入经纬度）
        if ("distance".equals(dto.getSortBy()) && dto.getLatitude() != null && dto.getLongitude() != null) {
            List<Ballroom> sorted = page.getRecords().stream()
                    .sorted((a, b) -> {
                        double distA = calcDistance(dto.getLatitude().doubleValue(), dto.getLongitude().doubleValue(),
                                a.getLatitude().doubleValue(), a.getLongitude().doubleValue());
                        double distB = calcDistance(dto.getLatitude().doubleValue(), dto.getLongitude().doubleValue(),
                                b.getLatitude().doubleValue(), b.getLongitude().doubleValue());
                        return Double.compare(distA, distB);
                    })
                    .collect(Collectors.toList());
            page.setRecords(sorted);
        }

        return page;
    }

    @Override
    public Ballroom getBallroomDetail(Long id) {
        Ballroom ballroom = getById(id);
        if (ballroom == null || ballroom.getStatus() != 1) {
            throw new BusinessException("球房不存在");
        }
        return ballroom;
    }

    @Override
    public void updateRating(Long ballroomId) {
        List<BallroomReview> reviews = reviewMapper.selectList(
                new LambdaQueryWrapper<BallroomReview>()
                        .eq(BallroomReview::getBallroomId, ballroomId));
        if (!reviews.isEmpty()) {
            BigDecimal avg = reviews.stream()
                    .map(BallroomReview::getRating)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);
            Ballroom ballroom = getById(ballroomId);
            ballroom.setRating(avg);
            ballroom.setRatingCount(reviews.size());
            updateById(ballroom);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleFavorite(Long userId, Long ballroomId) {
        BallroomFavorite existing = favoriteMapper.selectOne(
                new LambdaQueryWrapper<BallroomFavorite>()
                        .eq(BallroomFavorite::getUserId, userId)
                        .eq(BallroomFavorite::getBallroomId, ballroomId));
        if (existing != null) {
            favoriteMapper.deleteById(existing.getId());
            return false; // 取消收藏
        } else {
            BallroomFavorite fav = new BallroomFavorite();
            fav.setUserId(userId);
            fav.setBallroomId(ballroomId);
            favoriteMapper.insert(fav);
            return true; // 收藏成功
        }
    }

    @Override
    public boolean checkFavorite(Long userId, Long ballroomId) {
        Long count = favoriteMapper.selectCount(
                new LambdaQueryWrapper<BallroomFavorite>()
                        .eq(BallroomFavorite::getUserId, userId)
                        .eq(BallroomFavorite::getBallroomId, ballroomId));
        return count > 0;
    }

    @Override
    public List<Ballroom> getMyFavorites(Long userId) {
        List<BallroomFavorite> favorites = favoriteMapper.selectList(
                new LambdaQueryWrapper<BallroomFavorite>()
                        .eq(BallroomFavorite::getUserId, userId)
                        .orderByDesc(BallroomFavorite::getCreateTime));
        List<Long> ballroomIds = favorites.stream()
                .map(BallroomFavorite::getBallroomId)
                .collect(Collectors.toList());
        if (ballroomIds.isEmpty()) {
            return List.of();
        }
        return listByIds(ballroomIds);
    }

    /**
     * 计算两点之间的距离（米），使用简化的 Haversine 公式
     */
    private double calcDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2)
                        + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        return s * 6371000; // 地球半径（米）
    }
}













