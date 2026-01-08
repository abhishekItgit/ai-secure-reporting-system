package com.backend.reporting.ai;

import com.backend.reporting.ai.dto.OpenAiRequest;
import com.backend.reporting.ai.dto.OpenAiResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class OpenAiClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public OpenAiClient(
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.model}") String model,
            ObjectMapper objectMapper
    ) {
        this.model = model;
        this.objectMapper = objectMapper;

        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public ReportIntent getIntent(String userQuery) {

        try {
            String prompt = PromptBuilder.build(userQuery);

            OpenAiRequest request = new OpenAiRequest(
                    model,
                    List.of(new OpenAiRequest.Message("user", prompt))
            );

            // Call OpenAI
            String rawResponse = webClient.post()
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("OpenAI error: " + body))
                    )
                    .bodyToMono(String.class)
                    .block();

            // Parse OpenAI response
            OpenAiResponse response =
                    objectMapper.readValue(rawResponse, OpenAiResponse.class);

            if (response.getChoices() == null || response.getChoices().isEmpty()) {
                throw new IllegalStateException("No choices returned from OpenAI");
            }

            String content = response.getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

            //  Clean AI output (important)
            String cleanJson = extractJson(content);

            return objectMapper.readValue(cleanJson, ReportIntent.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get intent from OpenAI", e);
        }
    }

    /**
     * Extracts valid JSON from AI output
     * (handles cases where AI adds extra text)
     */
    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");

        if (start == -1 || end == -1 || start >= end) {
            throw new IllegalStateException("Invalid JSON returned by OpenAI: " + text);
        }
        return text.substring(start, end + 1);
    }
}
