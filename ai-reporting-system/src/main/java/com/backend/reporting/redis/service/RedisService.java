package com.backend.reporting.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;

@Service
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void safeSet(String key, Object value, Duration ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (Exception ex) {
            logger.warn("Failed to save key {} in Redis", key, ex);
        }
    }

    public Optional<Object> safeGet(String key) {
        try {
            return Optional.ofNullable(redisTemplate.opsForValue().get(key));
        } catch (Exception ex) {
            logger.warn("Failed to read key {} from Redis", key, ex);
            return Optional.empty();
        }
    }

    public void safeDelete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception ex) {
            logger.warn("Failed to delete key {} from Redis", key, ex);
        }
    }
}
