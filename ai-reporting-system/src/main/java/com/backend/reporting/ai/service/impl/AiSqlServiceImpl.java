package com.backend.reporting.ai.service.impl;



import com.backend.reporting.ai.prompt.SqlPromptBuilder;
import com.backend.reporting.ai.security.SqlSafetyValidator;
import com.backend.reporting.ai.service.AiSqlService;
import com.backend.reporting.ai.service.IOpenAiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiSqlServiceImpl implements AiSqlService {

    private final SqlPromptBuilder promptBuilder;
    private final SqlSafetyValidator sqlSafetyValidator;
    private final IOpenAiClient openAiClient;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public AiSqlServiceImpl(
            SqlPromptBuilder promptBuilder,
            SqlSafetyValidator sqlSafetyValidator,
            IOpenAiClient openAiClient
    ) {
        this.promptBuilder = promptBuilder;
        this.sqlSafetyValidator = sqlSafetyValidator;
        this.openAiClient = openAiClient;
    }

    @Override
    public String generateSql(
            String databaseSchema,
            String userRequest,
            String conversationMemory
    ) {
        String prompt = promptBuilder.buildPrompt(
                databaseSchema,
                userRequest,
                conversationMemory
        );

        String aiResponse = openAiClient.complete(prompt, model);

        String sql = aiResponse.trim();

        sqlSafetyValidator.validate(sql);

        return sql;
    }
}

