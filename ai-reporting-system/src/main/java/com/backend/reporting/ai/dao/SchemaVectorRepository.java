package com.backend.reporting.ai.dao;

import com.backend.reporting.ai.dto.SchemaContextChunk;
import com.backend.reporting.ai.dto.SchemaMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class SchemaVectorRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JdbcTemplate jdbcTemplate;
    private final String schemaName;

    public SchemaVectorRepository(
            @Qualifier("pgJdbcTemplate") JdbcTemplate jdbcTemplate,
            @Value("${schema.metadata.name}") String schemaName
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.schemaName = schemaName;
    }


    public void upsert(
            UUID id,
            String tableName,
            String content,
            float[] embedding
    ) {
        SchemaMetadata metadata = new SchemaMetadata(
                schemaName,
                tableName,
                "TABLE_SCHEMA",
                "mysql",
                1
        );
        String metadataJson;
        try {
            metadataJson = objectMapper.writeValueAsString(metadata);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to serialize schema metadata", ex);
        }

        String sql = """
    INSERT INTO db_context
    (id, type, content, metadata, embedding)
    VALUES (?, ?, ?, ?::jsonb, ?::vector)
    ON CONFLICT (id)
    DO UPDATE SET
      content = EXCLUDED.content,
      embedding = EXCLUDED.embedding,
      metadata = EXCLUDED.metadata,
      created_at = now()
""";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, id);                       // UUID
            ps.setString(2, "TABLE_SCHEMA");           // type
            ps.setString(3, content);                  // content
            ps.setString(4, metadataJson);             // json string
            ps.setString(5, toPgVector(embedding));    // "[0.01,0.02,...]"
            return ps;
        });
    }

    private String toPgVector(float[] embedding) {
        return IntStream.range(0, embedding.length)
                .mapToObj(i -> Float.toString(embedding[i]))
                .collect(Collectors.joining(",", "[", "]"));
    }


    public List<SchemaContextChunk> findRelevantSchema(
            float[] embedding,
            List<String> keywords,
            int limit
    ) {

        String keywordClause = "";
        if (keywords != null && !keywords.isEmpty()) {
            String conditions = keywords.stream()
                    .map(keyword -> "content ILIKE ?")
                    .collect(Collectors.joining(" OR "));
            keywordClause = " AND (" + conditions + ")";
        }

        String sql = """
                WITH base_tables AS (
                    SELECT content, metadata, embedding
                    FROM db_context
                    WHERE type = 'TABLE_SCHEMA' %s
                ),

                fk_names AS (
                    SELECT DISTINCT
                           (regexp_match(content, '->\\\\s*(\\\\w+)', 'i'))[1] AS fk_table
                    FROM base_tables
                    WHERE content ~* '->'
                ),

                fk_tables AS (
                    SELECT dc.content, dc.metadata, dc.embedding
                    FROM db_context dc
                    JOIN fk_names f
                      ON dc.content ILIKE '%Table: ' || f.fk_table || '%'
                ),

                all_tables AS (
                    SELECT content, metadata, embedding FROM base_tables
                    UNION ALL
                    SELECT content, metadata, embedding FROM fk_tables
                )

                SELECT content, metadata
                FROM (
                    SELECT DISTINCT content, metadata, embedding
                    FROM all_tables
                ) t
                ORDER BY embedding <-> CAST(? AS vector)
                LIMIT ?;
                """.formatted(keywordClause);

        return jdbcTemplate.query(
                sql,
                ps -> {
                    int index = 1;
                    if (keywords != null) {
                        for (String keyword : keywords) {
                            ps.setString(index++, "%" + keyword + "%");
                        }
                    }
                    ps.setString(index++, toPgVector(embedding));
                    ps.setInt(index, limit);
                },
                (rs, rowNum) -> {
                    String content = rs.getString("content");

                    String metadataJson = rs.getString("metadata");
                    Map<String, Object> metadata;
                    try {
                        metadata = objectMapper.readValue(metadataJson, Map.class);
                    } catch (Exception ex) {
                        throw new IllegalStateException("Failed to parse schema metadata", ex);
                    }

                    return new SchemaContextChunk(content, metadata);
                }
        );
    }


}
