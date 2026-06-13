
















package com.billiards.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.billiards.common.Result;
import com.billiards.dto.ChallengeCreateDTO;
import com.billiards.dto.ChallengeDetailVO;
import com.billiards.dto.ChallengeFinishDTO;
import com.billiards.dto.SignupHandleDTO;
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
     * POST /api/challenge/create
     */
    @PostMapping("/create")
    public Result<Challenge> create(@RequestHeader("userId") Long userId,
                                     @Valid @RequestBody ChallengeCreateDTO dto) {
        Challenge challenge = challengeService.createChallenge(
                userId, dto.getBallroomId(), dto.getBallType(),
                dto.getFormatType(), dto.getFormatValue(),
                dto.getStartTime(), dto.getMaxPlayers(), dto.getRemark());
        return Result.success(challenge);
    }

    /**
     * 约战分页列表
     * GET /api/challenge/page?page=1&size=10&ballType=&status=
     */
    @GetMapping("/page")
    public Result<Page<Challenge>> page(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) Integer ballType,
                                         @RequestParam(required = false) Integer status) {
        return Result.success(challengeService.getChallengePage(page, size, ballType, status));
    }

    /**
     * 约战详情
     * GET /api/challenge/{id}?userId=1
     */
    @GetMapping("/{id}")
    public Result<ChallengeDetailVO> detail(@PathVariable Long id,
                                             @RequestParam(required = false) Long userId) {
        ChallengeDetailVO vo = challengeService.getChallengeDetail(id);
        if (userId != null) {
            vo.setIsSignedUp(signupService.isUserSignedUp(id, userId));
            vo.setIsInitiator(vo.getInitiatorId().equals(userId));
        }
        return Result.success(vo);
    }

    /**
     * 取消约战
     * POST /api/challenge/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@RequestHeader("userId") Long userId,
                                @PathVariable Long id) {
        challengeService.cancelChallenge(userId, id);
        return Result.success();
    }

    /**
     * 完成约战（提交比分）
     * POST /api/challenge/{id}/finish
     */
    @PostMapping("/{id}/finish")
    public Result<Void> finish(@RequestHeader("userId") Long userId,
                                @PathVariable Long id,
                                @Valid @RequestBody ChallengeFinishDTO dto) {
        challengeService.finishChallenge(id, userId, dto.getScoreInitiator(),
                dto.getScoreOpponent(), dto.getWinnerId());
        return Result.success();
    }

    /**
     * 报名约战
     * POST /api/challenge/{id}/signup
     */
    @PostMapping("/{id}/signup")
    public Result<Void> signup(@RequestHeader("userId") Long userId,
                                @PathVariable Long id) {
        signupService.signup(id, userId);
        return Result.success();
    }

    /**
     * 确认报名
     * POST /api/challenge/{id}/signup/confirm
     */
    @PostMapping("/{id}/signup/confirm")
    public Result<Void> confirmSignup(@RequestHeader("userId") Long userId,
                                       @PathVariable Long id,
                                       @Valid @RequestBody SignupHandleDTO dto) {
        signupService.confirmSignup(id, userId, dto.getSignupId());
        return Result.success();
    }

    /**
     * 拒绝报名
     * POST /api/challenge/{id}/signup/reject
     */
    @PostMapping("/{id}/signup/reject")
    public Result<Void> rejectSignup(@RequestHeader("userId") Long userId,
                                      @PathVariable Long id,
                                      @Valid @RequestBody SignupHandleDTO dto) {
        signupService.rejectSignup(id, userId, dto.getSignupId());
        return Result.success();
    }

    /**
     * 我的约战列表
     * GET /api/challenge/my/{userId}?type=1&page=1&size=10
     * type: 1-我发起的 2-我参加的 3-历史的
     */
    @GetMapping("/my/{userId}")
    public Result<Page<Challenge>> myChallenges(@PathVariable Long userId,
                                                  @RequestParam(defaultValue = "1") Integer type,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(challengeService.getMyChallenges(userId, type, page, size));
    }
}
















