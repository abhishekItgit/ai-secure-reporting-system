package com.backend.reporting.ai.service.impl;



import com.backend.reporting.ai.prompt.SqlPromptBuilder;
import com.backend.reporting.ai.schema.SchemaProvider;
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
    private final SchemaProvider schemaProvider;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public AiSqlServiceImpl(
            SqlPromptBuilder promptBuilder,
            SqlSafetyValidator sqlSafetyValidator,
            IOpenAiClient openAiClient,
            SchemaProvider schemaProvider
    ) {
        this.promptBuilder = promptBuilder;
        this.sqlSafetyValidator = sqlSafetyValidator;
        this.openAiClient = openAiClient;
        this.schemaProvider = schemaProvider;
    }

    @Override
    public String generateSql(
         //   String schema,
            String userRequest,
            String conversationMemory
    ) {
        String schema = schemaProvider.getSchema();
        String prompt = promptBuilder.buildPrompt(
                schema,
                userRequest,
                conversationMemory
        );

        String aiResponse = openAiClient.complete(prompt, model);

        String sql = aiResponse.trim();

        sqlSafetyValidator.validate(sql);

        return sql;
    }
}

