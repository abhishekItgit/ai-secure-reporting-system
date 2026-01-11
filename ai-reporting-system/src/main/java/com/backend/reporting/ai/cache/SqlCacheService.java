package com.backend.reporting.ai.cache;

import com.backend.reporting.redis.service.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class SqlCacheService {

    private static final Duration CACHE_TTL = Duration.ofHours(2);
    private static final String PREFIX = "ai:sql:cache:";

    private final RedisService redisService;

    public SqlCacheService(RedisService redisService) {
        this.redisService = redisService;
    }

    public Optional<String> get(String cacheKey) {
        return redisService.safeGet(PREFIX + cacheKey)
                .map(Object::toString);
    }

    public void put(String cacheKey, String sql) {
        redisService.safeSet(
                PREFIX + cacheKey,
                sql,
                CACHE_TTL
        );
    }
}
