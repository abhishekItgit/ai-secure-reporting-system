package com.backend.reporting.ai.prompt;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SqlPromptBuilder {

    private static final String SYSTEM_PROMPT_PATH =
            "ai/sql-agent-system-prompt.txt";

    private String systemPrompt;

    @PostConstruct
    public void init() {
        this.systemPrompt = loadSystemPrompt();
    }

    public String buildPrompt(
            String databaseSchema,
            String userRequest,
            String conversationMemory
    ) {
        StringBuilder finalPrompt = new StringBuilder();

        finalPrompt.append(systemPrompt).append("\n\n");

        finalPrompt.append("DATABASE SCHEMA:\n")
                .append(databaseSchema)
                .append("\n\n");

        if (conversationMemory != null && !conversationMemory.isBlank()) {
            finalPrompt.append("PREVIOUS LEARNED RULES:\n")
                    .append(conversationMemory)
                    .append("\n\n");
        }

        finalPrompt.append("USER REQUEST:\n")
                .append(userRequest);

        return finalPrompt.toString();
    }

    private String loadSystemPrompt() {
        try {
            ClassPathResource resource =
                    new ClassPathResource(SYSTEM_PROMPT_PATH);

            return new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load SQL AI system prompt", e
            );
        }
    }
}
