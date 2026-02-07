package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.dto.EmbeddingRequest;
import com.backend.reporting.ai.dto.EmbeddingResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class OpenAiEmbeddingClient {
    private static final Logger logger = LoggerFactory.getLogger(OpenAiEmbeddingClient.class);
    private final WebClient webClient;

    public OpenAiEmbeddingClient(OpenAiWebClient openAiWebClient){
        this.webClient = openAiWebClient.client();
    }

    public float[] embedding(String text){
        EmbeddingRequest request = new EmbeddingRequest( "text-embedding-3-small", List.of(text));
        EmbeddingResponse response = webClient.post().uri("/embeddings")
                .headers(h -> {
                    h.setContentType(MediaType.APPLICATION_JSON);
                })
                                 .bodyValue(request)
                                 .retrieve()
                                 .bodyToMono(EmbeddingResponse.class)
                                 .block();
        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            logger.error("OpenAI embedding response was empty");
            throw new IllegalStateException("Embedding response was empty");
        }
        return response.getData()
                .get(0)
                .getEmbedding();

    }
}
