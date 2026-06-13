








package com.billiards.common;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token 管理器（简化版，生产环境建议用 Redis + JWT）
 * 本地开发使用内存存储
 */
@Component
public class TokenManager {

    /** token -> userId 映射 */
    private final Map<String, Long> tokenMap = new ConcurrentHashMap<>();

    /** userId -> token 映射 */
    private final Map<Long, String> userTokenMap = new ConcurrentHashMap<>();

    /**
     * 为用户生成 token
     */
    public String generateToken(Long userId) {
        // 如果已有 token，先移除旧的
        String oldToken = userTokenMap.get(userId);
        if (oldToken != null) {
            tokenMap.remove(oldToken);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenMap.put(token, userId);
        userTokenMap.put(userId, token);
        return token;
    }

    /**
     * 根据 token 获取用户 ID
     */
    public Long getUserId(String token) {
        return tokenMap.get(token);
    }

    /**
     * 移除 token（退出登录）
     */
    public void removeToken(String token) {
        Long userId = tokenMap.remove(token);
        if (userId != null) {
            userTokenMap.remove(userId);
        }
    }
}









