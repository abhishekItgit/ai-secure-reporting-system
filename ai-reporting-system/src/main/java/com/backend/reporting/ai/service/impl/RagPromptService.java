package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.dto.SchemaContextChunk;
import com.backend.reporting.ai.prompt.RagPromptBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagPromptService {
    private final QuestionEmbeddingService questionEmbeddingService;
    private final SchemaContextService schemaRetrievalService;
    private final RagPromptBuilder ragPromptBuilder;

    public RagPromptService(
            QuestionEmbeddingService questionEmbeddingService,
            SchemaContextService schemaRetrievalService,
            RagPromptBuilder ragPromptBuilder
    ) {
        this.questionEmbeddingService = questionEmbeddingService;
        this.schemaRetrievalService = schemaRetrievalService;
        this.ragPromptBuilder = ragPromptBuilder;
    }

    public String buildPromptForQuestion(String question) {

        float[] embedding =
                questionEmbeddingService.embedQuestion(question);

        List<SchemaContextChunk> schemaChunks =
                schemaRetrievalService.retrieveSchema(question,embedding);

        return ragPromptBuilder.buildPrompt(question, schemaChunks);
    }
}
