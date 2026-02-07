package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.model.HashUtil;
import com.backend.reporting.ai.security.SqlSafetyValidator;
import com.backend.reporting.ai.service.IOpenAiClient;
import com.backend.reporting.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SqlGenerationService {

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    private final RedisService redisService;
    private final IOpenAiClient iOpenAiClient;
    private final HashUtil hashUtil;
    private  final SqlSafetyValidator sqlSafetyValidator;


    public SqlGenerationService(RedisService redisService,IOpenAiClient iOpenAiClient,HashUtil hashUtil,SqlSafetyValidator sqlSafetyValidator){
        this.hashUtil = hashUtil;
        this.redisService = redisService;
        this.iOpenAiClient = iOpenAiClient;
        this.sqlSafetyValidator = sqlSafetyValidator;
    }
    public String generateSql(String prompt) {
        String key = "sql:v1:" + hashUtil.sha256(prompt);
        Object cached = redisService.safeGet(key).orElse(null);
        if (cached instanceof String cachedSql) {
            return cachedSql;
        }
        String rawResponse = iOpenAiClient.complete(prompt,model);
        String sql = normalizeSql(rawResponse);

        sqlSafetyValidator.validate(sql);
        redisService.safeSet(key, sql, Duration.ofHours(2));
        return sql;



    }
    private String normalizeSql(String response) {
        return response
                .replaceAll("(?i)```sql", "")
                .replaceAll("```", "")
                .trim();

    }


}
