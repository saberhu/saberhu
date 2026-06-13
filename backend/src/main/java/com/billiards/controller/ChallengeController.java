


















package com.billiards.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billiards.common.Result;
import com.billiards.dto.ChallengeCreateDTO;
import com.billiards.dto.ChallengePageDTO;
import com.billiards.entity.Challenge;
import com.billiards.service.ChallengeService;
import com.billiards.service.ChallengeSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 约战控制器
 */
@RestController
@RequestMapping("/api/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ChallengeSignupService signupService;

    /**
     * 创建约战
     */
    @PostMapping("/create")
    public Result<Challenge> create(@RequestParam Long userId,
                                     @Valid @RequestBody ChallengeCreateDTO dto) {
        Challenge challenge = challengeService.createChallenge(
                userId, dto.getBallroomId(), dto.getBallType(),
                dto.getFormatType(), dto.getFormatValue(),
                dto.getStartTime(), dto.getMaxPlayers(), dto.getRemark());
        return Result.success(challenge);
    }

    /**
     * 约战列表（分页）
     */
    @GetMapping("/page")
    public Result<Page<Challenge>> page(ChallengePageDTO dto) {
        LambdaQueryWrapper<Challenge> wrapper = new LambdaQueryWrapper<>();
        if (dto.getStatus() != null) {
            wrapper.eq(Challenge::getStatus, dto.getStatus());
        }
        if (dto.getBallroomId() != null) {
            wrapper.eq(Challenge::getBallroomId, dto.getBallroomId());
        }
        wrapper.orderByDesc(Challenge::getCreateTime);

        Page<Challenge> result = challengeService.page(
                new Page<>(dto.getPage(), dto.getSize()), wrapper);
        return Result.success(result);
    }

    /**
     * 约战详情
     */
    @GetMapping("/{id}")
    public Result<Challenge> detail(@PathVariable Long id) {
        Challenge challenge = challengeService.getById(id);
        if (challenge == null) {
            return Result.notFound("约战不存在");
        }
        return Result.success(challenge);
    }

    /**
     * 取消约战
     */
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@RequestParam Long userId, @PathVariable Long id) {
        challengeService.cancelChallenge(userId, id);
        return Result.success();
    }

    /**
     * 完成约战
     */
    @PostMapping("/{id}/finish")
    public Result<Void> finish(@PathVariable Long id,
                                @RequestParam Integer scoreInitiator,
                                @RequestParam Integer scoreOpponent,
                                @RequestParam(required = false) Long winnerId) {
        challengeService.finishChallenge(id, scoreInitiator, scoreOpponent, winnerId);
        return Result.success();
    }

    /**
     * 报名约战
     */
    @PostMapping("/{id}/signup")
    public Result<Void> signup(@PathVariable Long id, @RequestParam Long userId) {
        signupService.signup(id, userId);
        return Result.success();
    }
}

















