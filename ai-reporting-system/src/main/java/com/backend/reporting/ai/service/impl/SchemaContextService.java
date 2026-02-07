package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.dao.SchemaVectorRepository;
import com.backend.reporting.ai.dto.SchemaContextChunk;
import com.backend.reporting.ai.model.ColumnMetadata;
import com.backend.reporting.ai.model.HashUtil;
import com.backend.reporting.ai.model.SchemaContext;
import com.backend.reporting.ai.schema.SchemaContextBuilder;
import com.backend.reporting.redis.service.RedisService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SchemaContextService {
    private static final int TOP_K = 65 ;
    private final MetadataService metadataService;
    private final MetadataGroupingService groupingService;
    private final SchemaContextBuilder contextBuilder;
    private final HashUtil hashUtil;
    private final RedisService redisService;
    private  final SchemaVectorRepository schemaVectorRepository;
    private static final Duration CACHE_TTL = Duration.ofHours(2);

    public SchemaContextService(
            MetadataService metadataService,
            MetadataGroupingService groupingService,
            SchemaContextBuilder contextBuilder,
            HashUtil hashUtil,
            RedisService redisService,
            SchemaVectorRepository schemaVectorRepository
    ) {
        this.metadataService = metadataService;
        this.groupingService = groupingService;
        this.contextBuilder = contextBuilder;
        this.hashUtil = hashUtil;
        this.redisService = redisService;
        this.schemaVectorRepository = schemaVectorRepository;
    }
    public List<SchemaContext> buildSchemaContexts(String dbName) {

     /*   List<ColumnMetadata> metadata =
                metadataService.loadMetaData(dbName);

        Map<String, List<ColumnMetadata>> grouped =
                groupingService.groupMetaData(metadata);*/


        List<SchemaContext> contexts = new ArrayList<>();

      /*  for (Map.Entry<String, List<ColumnMetadata>> entry : grouped.entrySet()) {
            contexts.add(
                    contextBuilder.build(entry.getKey(), entry.getValue())
            );
        }*/
        contexts = metadataService.fetchContext();

        return contexts;
    }
    public List<SchemaContextChunk> retrieveSchema(
            String question,
            float[] embedding
    ) {

        // Redis key based on question
        String cacheKey =
                "schema:v1:" + hashUtil.sha256(question);

        // Try Redis first
        Object cached = redisService.safeGet(cacheKey);

        List<SchemaContextChunk> chunks =
                schemaVectorRepository.findRelevantSchema(
                        embedding,
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

    public void fetchContext(){

    }

}
