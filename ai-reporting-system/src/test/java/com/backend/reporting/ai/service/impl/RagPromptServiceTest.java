package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.dto.SchemaContextChunk;
import com.backend.reporting.ai.prompt.RagPromptBuilder;
import com.backend.reporting.ai.retrieval.QueryKeywordExtractor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RagPromptServiceTest {

    @Mock
    private QuestionEmbeddingService questionEmbeddingService;

    @Mock
    private SchemaContextService schemaContextService;

    private final QueryKeywordExtractor keywordExtractor = new QueryKeywordExtractor();

    private final RagPromptBuilder ragPromptBuilder = new RagPromptBuilder(
            chunks -> "Table: users(id, name)"
    );

    @Test
    void buildsPromptWithKeywordsAndSchema() {
        when(questionEmbeddingService.embedQuestion("show user names"))
                .thenReturn(new float[]{0.1f, 0.2f});
        when(schemaContextService.retrieveSchema("show user names", new float[]{0.1f, 0.2f}))
                .thenReturn(List.of(new SchemaContextChunk("Table: users", Map.of())));

        RagPromptService ragPromptService = new RagPromptService(
                questionEmbeddingService,
                schemaContextService,
                ragPromptBuilder,
                keywordExtractor
        );

        String prompt = ragPromptService.buildPromptForQuestion("show user names");

        assertTrue(prompt.contains("Extracted Keywords:"));
        assertTrue(prompt.contains("users"));
    }
}
