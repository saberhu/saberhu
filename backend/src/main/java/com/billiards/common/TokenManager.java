
package com.billiards.common;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token 管理器（简化实现，生产环境建议用 Redis）
 */
public class TokenManager {

    private static final Map<String, Long> TOKEN_USER_MAP = new ConcurrentHashMap<>();

    /**
     * 生成 token
     */
    public static String generateToken(Long userId) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        TOKEN_USER_MAP.put(token, userId);
        return token;
    }

    /**
     * 根据 token 获取用户ID
     */
    public static Long getUserIdByToken(String token) {
        return TOKEN_USER_MAP.get(token);
    }

    /**
     * 校验 token 是否有效
     */
    public static boolean isValid(String token) {
        return token != null && TOKEN_USER_MAP.containsKey(token);
    }

    /**
     * 移除 token
     */
    public static void removeToken(String token) {
        TOKEN_USER_MAP.remove(token);
    }
}
