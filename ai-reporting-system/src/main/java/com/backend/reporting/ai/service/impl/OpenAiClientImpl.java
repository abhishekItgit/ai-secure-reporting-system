package com.backend.reporting.ai.service.impl;


import com.backend.reporting.ai.service.IOpenAiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiClientImpl implements IOpenAiClient {

    private final WebClient webClient;

    public OpenAiClientImpl(
            @Value("${openai.api.key}") String apiKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String complete(String prompt, String model) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0
        );

        try {
            Map response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return extractContent(response);

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {

            // Clear, production-grade error logging
            System.err.println("❌ OpenAI API ERROR");
            System.err.println("Status: " + e.getStatusCode());
            System.err.println("Response: " + e.getResponseBodyAsString());

            throw new IllegalStateException(
                    "OpenAI API error: " + e.getStatusCode().value()
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Unexpected OpenAI client error");
        }
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map response) {
        try {
            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) response.get("choices");

            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Invalid OpenAI response format: " + response
            );
        }
    }
}
