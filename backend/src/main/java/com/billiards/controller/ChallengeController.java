


















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
     * 发起约战
     * POST /api/challenge/create?userId=1
     * 请求参数：{ "ballroomId": 1, "ballType": 1, "formatType": 1, "formatValue": 5, "startTime": "2026-06-14 14:00:00", "maxPlayers": 2, "remark": "" }
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
     * 约战列表：支持按球种筛选，按时间排序
     * GET /api/challenge/page?page=1&size=10&ballType=1&status=0
     */
    @GetMapping("/page")
    public Result<Page<Challenge>> page(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) Integer ballType,
                                         @RequestParam(required = false) Integer status) {
        Page<Challenge> result = challengeService.getChallengePage(page, size, ballType, status);
        return Result.success(result);
    }

    /**
     * 约战详情（含发起人、球房、报名信息）
     * GET /api/challenge/{id}
     */
    @GetMapping("/{id}")
    public Result<ChallengeDetailVO> detail(@PathVariable Long id) {
        ChallengeDetailVO vo = challengeService.getChallengeDetail(id);
        return Result.success(vo);
    }

    /**
     * 取消约战（仅发起人）
     * POST /api/challenge/{id}/cancel?userId=1
     */
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@RequestParam Long userId, @PathVariable Long id) {
        challengeService.cancelChallenge(userId, id);
        return Result.success();
    }

    /**
     * 结束约战提交比分（仅发起人）
     * POST /api/challenge/{id}/finish?userId=1
     * 请求参数：{ "scoreInitiator": 5, "scoreOpponent": 3, "winnerId": 1 }
     */
    @PostMapping("/{id}/finish")
    public Result<Void> finish(@PathVariable Long id,
                                @RequestParam Long userId,
                                @Valid @RequestBody ChallengeFinishDTO dto) {
        challengeService.finishChallenge(id, userId, dto.getScoreInitiator(),
                dto.getScoreOpponent(), dto.getWinnerId());
        return Result.success();
    }

    /**
     * 报名约战
     * POST /api/challenge/{id}/signup?userId=2
     */
    @PostMapping("/{id}/signup")
    public Result<Void> signup(@PathVariable Long id, @RequestParam Long userId) {
        signupService.signup(id, userId);
        return Result.success();
    }

    /**
     * 确认报名（仅发起人）
     * POST /api/challenge/{id}/signup/confirm?userId=1
     * 请求参数：{ "signupId": 1 }
     */
    @PostMapping("/{id}/signup/confirm")
    public Result<Void> confirmSignup(@PathVariable Long id,
                                       @RequestParam Long userId,
                                       @Valid @RequestBody SignupHandleDTO dto) {
        signupService.confirmSignup(id, userId, dto.getSignupId());
        return Result.success();
    }

    /**
     * 拒绝报名（仅发起人）
     * POST /api/challenge/{id}/signup/reject?userId=1
     * 请求参数：{ "signupId": 1 }
     */
    @PostMapping("/{id}/signup/reject")
    public Result<Void> rejectSignup(@PathVariable Long id,
                                      @RequestParam Long userId,
                                      @Valid @RequestBody SignupHandleDTO dto) {
        signupService.rejectSignup(id, userId, dto.getSignupId());
        return Result.success();
    }

    /**
     * 我的约战列表
     * GET /api/challenge/my?userId=1&type=1&page=1&size=10
     * type: 1-我发起的 2-我参加的 3-历史的
     */
    @GetMapping("/my")
    public Result<Page<Challenge>> myChallenges(@RequestParam Long userId,
                                                  @RequestParam(defaultValue = "1") Integer type,
                                                  @RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        Page<Challenge> result = challengeService.getMyChallenges(userId, type, page, size);
        return Result.success(result);
    }
}

















