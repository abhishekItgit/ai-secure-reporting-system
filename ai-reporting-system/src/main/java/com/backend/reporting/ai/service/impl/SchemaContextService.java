package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.dao.SchemaVectorRepository;
import com.backend.reporting.ai.dto.SchemaContextChunk;
import com.backend.reporting.ai.model.HashUtil;
import com.backend.reporting.ai.model.SchemaContext;
import com.backend.reporting.ai.retrieval.QueryKeywordExtractor;
import com.backend.reporting.redis.service.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class SchemaContextService {
    private static final int TOP_K = 65;
    private final MetadataService metadataService;
    private final QueryKeywordExtractor keywordExtractor;
    private final HashUtil hashUtil;
    private final RedisService redisService;
    private final SchemaVectorRepository schemaVectorRepository;
    private static final Duration CACHE_TTL = Duration.ofHours(2);

    public SchemaContextService(
            MetadataService metadataService,
            QueryKeywordExtractor keywordExtractor,
            HashUtil hashUtil,
            RedisService redisService,
            SchemaVectorRepository schemaVectorRepository
    ) {
        this.metadataService = metadataService;
        this.keywordExtractor = keywordExtractor;
        this.hashUtil = hashUtil;
        this.redisService = redisService;
        this.schemaVectorRepository = schemaVectorRepository;
    }
    public List<SchemaContext> buildSchemaContexts(String dbName) {
        return metadataService.fetchContext();
    }
    public List<SchemaContextChunk> retrieveSchema(
            String question,
            float[] embedding
    ) {

        List<String> keywords = keywordExtractor.extract(question);
        String cacheKey = "schema:v2:" + hashUtil.sha256(
                question + "|" + String.join(",", keywords)
        );

        Object cached = redisService.safeGet(cacheKey).orElse(null);
        if (cached instanceof List<?> cachedList && !cachedList.isEmpty()) {
            return cachedList.stream()
                    .filter(SchemaContextChunk.class::isInstance)
                    .map(SchemaContextChunk.class::cast)
                    .toList();
        }

        List<SchemaContextChunk> chunks = schemaVectorRepository.findRelevantSchema(
                embedding,
                keywords,
                TOP_K
        );

        if (chunks == null || chunks.isEmpty()) {
            return List.of();
        }

        //Cache result
        redisService.safeSet(
                cacheKey,
                chunks,
                CACHE_TTL
        );

        return chunks;
    }

}
