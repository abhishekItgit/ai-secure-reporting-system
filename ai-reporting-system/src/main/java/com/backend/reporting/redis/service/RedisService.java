package com.backend.reporting.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void safeSet(String key, Object value, Duration ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (Exception ignored) {
            System.out.println("error while saving in Redis"+ ignored.getMessage());
            ignored.printStackTrace();
        }
    }

    public Optional<Object> safeGet(String key) {
        try {
            return Optional.ofNullable(redisTemplate.opsForValue().get(key));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void safeDelete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception ignored) {}
    }
}
