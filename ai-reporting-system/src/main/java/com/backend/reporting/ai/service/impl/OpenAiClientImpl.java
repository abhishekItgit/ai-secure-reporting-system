package com.backend.reporting.ai.service.impl;


import com.backend.reporting.ai.service.IOpenAiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiClientImpl implements IOpenAiClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiClientImpl.class);
    private final WebClient webClient;

    public OpenAiClientImpl(OpenAiWebClient openAiWebClient) {
       this.webClient = openAiWebClient.client();
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
                    .uri("/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return extractContent(response);

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            logger.error("OpenAI API error status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());

            throw new IllegalStateException(
                    "OpenAI API error: " + e.getStatusCode().value()
            );

        } catch (Exception e) {
            logger.error("Unexpected OpenAI client error", e);
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
