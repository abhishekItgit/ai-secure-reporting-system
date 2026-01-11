package com.backend.reporting.ai.service.impl;


import com.backend.reporting.ai.cache.*;
import com.backend.reporting.ai.security.SqlSafetyValidator;
import com.backend.reporting.ai.service.AiSqlService;
import org.springframework.stereotype.Service;

@Service
public class AiOrchestratorService {

    private final SqlCacheService sqlCacheService;
    private final AiSqlService aiSqlService;
    private final SqlSafetyValidator sqlSafetyValidator;
    private final SchemaHashProvider schemaHashProvider;

    public AiOrchestratorService(SqlCacheService sqlCacheService, AiSqlService aiSqlService, SqlSafetyValidator sqlSafetyValidator, SchemaHashProvider schemaHashProvider) {
        this.sqlCacheService = sqlCacheService;
        this.aiSqlService = aiSqlService;
        this.sqlSafetyValidator = sqlSafetyValidator;
        this.schemaHashProvider = schemaHashProvider;
    }

    public String process(String userQuestion) {

        //  Normalize
        String normalized = QueryNormalizer.normalize(userQuestion);

        // Build cache key
        String cacheKey = SqlCacheKeyBuilder.build(schemaHashProvider.getSchemaHash(), normalized);

        // Redis HIT → return immediately
        var cachedSql = sqlCacheService.get(cacheKey);
        if (cachedSql.isPresent()) {
            sqlSafetyValidator.validate(cachedSql.get());
            return cachedSql.get();
        }

        // 4Redis MISS → call AI
        String sql = aiSqlService.generateSql(userQuestion, null);

        //  Validate
        sqlSafetyValidator.validate(sql);

        //Cache & return
        sqlCacheService.put(cacheKey, sql);
        System.out.println("cache is done");
        return sql;
    }
}
