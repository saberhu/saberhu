








package com.billiards.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billiards.entity.Ballroom;

/**
 * 球房服务接口
 */
public interface BallroomService extends IService<Ballroom> {

    /**
     * 更新球房评分（新增评价时调用）
     */
    void updateRating(Long ballroomId);
}








