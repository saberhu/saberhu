










package com.billiards.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 球房业务实现
 */
@Service
@RequiredArgsConstructor
public class BallroomServiceImpl implements BallroomService {

    private final BallroomMapper ballroomMapper;
    private final BallroomReviewMapper reviewMapper;
    private final BallroomFavoriteMapper favoriteMapper;

    @Override
    public Page<BallroomPageDTO> getBallroomPage(Integer page, Integer size, String keyword, Long userId) {
        Page<Ballroom> ballroomPage = new Page<>(page, size);
        LambdaQueryWrapper<Ballroom> wrapper = new LambdaQueryWrapper<Ballroom>()
                .eq(Ballroom::getStatus, 1);

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Ballroom::getName, keyword)
                    .or().like(Ballroom::getAddress, keyword);
        }

        wrapper.orderByDesc(Ballroom::getRating);
        Page<Ballroom> result = ballroomMapper.selectPage(ballroomPage, wrapper);

        // 转换为 DTO，附带收藏状态
        Page<BallroomPageDTO> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream().map(b -> {
            BallroomPageDTO dto = new BallroomPageDTO();
            dto.setId(b.getId());
            dto.setName(b.getName());
            dto.setAddress(b.getAddress());
            dto.setPhone(b.getPhone());
            dto.setPriceDesc(b.getPriceDesc());
            dto.setBusinessHours(b.getBusinessHours());
            dto.setLongitude(b.getLongitude());
            dto.setLatitude(b.getLatitude());
            dto.setImages(b.getImages());
            dto.setRating(b.getRating());
            dto.setRatingCount(b.getRatingCount());
            if (userId != null) {
                dto.setIsFavorite(favoriteMapper.selectCount(
                        new LambdaQueryWrapper<BallroomFavorite>()
                                .eq(BallroomFavorite::getUserId, userId)
                                .eq(BallroomFavorite::getBallroomId, b.getId())) > 0);
            }
            return dto;
        }).collect(Collectors.toList()));

        return dtoPage;
    }

    @Override
    public Ballroom getBallroomById(Long id) {
        Ballroom ballroom = ballroomMapper.selectById(id);
        if (ballroom == null) {
            throw new BusinessException(404, "球房不存在");
        }
        return ballroom;
    }

    @Override
    public Page<BallroomReview> getReviews(Long ballroomId, Integer page, Integer size) {
        Page<BallroomReview> reviewPage = new Page<>(page, size);
        LambdaQueryWrapper<BallroomReview> wrapper = new LambdaQueryWrapper<BallroomReview>()
                .eq(BallroomReview::getBallroomId, ballroomId)
                .orderByDesc(BallroomReview::getCreateTime);
        return reviewMapper.selectPage(reviewPage, wrapper);
    }

    @Override
    @Transactional
    public void submitReview(Long userId, Long ballroomId, BigDecimal rating, String content, String images) {
        Ballroom ballroom = ballroomMapper.selectById(ballroomId);
        if (ballroom == null) {
            throw new BusinessException(404, "球房不存在");
        }

        BallroomReview review = new BallroomReview();
        review.setBallroomId(ballroomId);
        review.setUserId(userId);
        review.setRating(rating);
        review.setContent(content != null ? content : "");
        review.setImages(images);
        reviewMapper.insert(review);

        // 更新球房评分
        LambdaQueryWrapper<BallroomReview> avgWrapper = new LambdaQueryWrapper<BallroomReview>()
                .eq(BallroomReview::getBallroomId, ballroomId);
        List<BallroomReview> allReviews = reviewMapper.selectList(avgWrapper);
        double avgRating = allReviews.stream()
                .mapToDouble(r -> r.getRating().doubleValue())
                .average().orElse(5.0);
        ballroom.setRating(BigDecimal.valueOf(Math.round(avgRating * 10) / 10.0));
        ballroom.setRatingCount(allReviews.size());
        ballroomMapper.updateById(ballroom);
    }

    @Override
    @Transactional
    public boolean toggleFavorite(Long userId, Long ballroomId) {
        BallroomFavorite existing = favoriteMapper.selectOne(
                new LambdaQueryWrapper<BallroomFavorite>()
                        .eq(BallroomFavorite::getUserId, userId)
                        .eq(BallroomFavorite::getBallroomId, ballroomId));
        if (existing != null) {
            favoriteMapper.deleteById(existing.getId());
            return false; // 已取消收藏
        } else {
            BallroomFavorite fav = new BallroomFavorite();
            fav.setUserId(userId);
            fav.setBallroomId(ballroomId);
            favoriteMapper.insert(fav);
            return true; // 已收藏
        }
    }

    @Override
    public boolean checkFavorite(Long userId, Long ballroomId) {
        return favoriteMapper.selectCount(
                new LambdaQueryWrapper<BallroomFavorite>()
                        .eq(BallroomFavorite::getUserId, userId)
                        .eq(BallroomFavorite::getBallroomId, ballroomId)) > 0;
    }

    @Override
    public List<Ballroom> getUserFavorites(Long userId) {
        List<BallroomFavorite> favorites = favoriteMapper.selectList(
                new LambdaQueryWrapper<BallroomFavorite>()
                        .eq(BallroomFavorite::getUserId, userId));
        List<Long> ids = favorites.stream()
                .map(BallroomFavorite::getBallroomId)
                .collect(Collectors.toList());
        if (ids.isEmpty()) return List.of();
        return ballroomMapper.selectBatchIds(ids);
    }

    @Override
    public List<Ballroom> getHotBallrooms(int limit) {
        LambdaQueryWrapper<Ballroom> wrapper = new LambdaQueryWrapper<Ballroom>()
                .eq(Ballroom::getStatus, 1)
                .orderByDesc(Ballroom::getRating)
                .last("LIMIT " + limit);
        return ballroomMapper.selectList(wrapper);
    }
}









