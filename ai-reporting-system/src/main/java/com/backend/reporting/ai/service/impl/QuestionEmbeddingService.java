package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.model.HashUtil;
import com.backend.reporting.redis.service.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class QuestionEmbeddingService {

    private final RedisService redisService;
    private final OpenAiEmbeddingClient embeddingClient;
    private final HashUtil hashUtil;


    public QuestionEmbeddingService(
            OpenAiEmbeddingClient embeddingClient,
            HashUtil hashUtil,RedisService redisService
    ) {
        this.embeddingClient = embeddingClient;
        this.hashUtil = hashUtil;
        this.redisService = redisService;
    }

    public float[] embedQuestion(String question) {
        String key  = "embedding:v1:" + hashUtil.sha256(question);

        /*Object cached = redisService.safeGet(key);
        if(cached != null ){return (float[])cached;}*/
        float[] embedding = embeddingClient.embedding(question);

        redisService.safeSet(key,embedding, Duration.ofHours(24));
      return embedding;

    }

    }
