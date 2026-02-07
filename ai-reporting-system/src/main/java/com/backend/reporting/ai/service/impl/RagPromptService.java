package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.dto.SchemaContextChunk;
import com.backend.reporting.ai.prompt.RagPromptBuilder;
import com.backend.reporting.ai.retrieval.QueryKeywordExtractor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagPromptService {
    private final QuestionEmbeddingService questionEmbeddingService;
    private final SchemaContextService schemaRetrievalService;
    private final RagPromptBuilder ragPromptBuilder;
    private final QueryKeywordExtractor keywordExtractor;

    public RagPromptService(
            QuestionEmbeddingService questionEmbeddingService,
            SchemaContextService schemaRetrievalService,
            RagPromptBuilder ragPromptBuilder,
            QueryKeywordExtractor keywordExtractor
    ) {
        this.questionEmbeddingService = questionEmbeddingService;
        this.schemaRetrievalService = schemaRetrievalService;
        this.ragPromptBuilder = ragPromptBuilder;
        this.keywordExtractor = keywordExtractor;
    }

    public String buildPromptForQuestion(String question) {

        float[] embedding =
                questionEmbeddingService.embedQuestion(question);

        List<SchemaContextChunk> schemaChunks =
                schemaRetrievalService.retrieveSchema(question,embedding);

        List<String> keywords = keywordExtractor.extract(question);
        return ragPromptBuilder.buildPrompt(question, keywords, schemaChunks);
    }
}
