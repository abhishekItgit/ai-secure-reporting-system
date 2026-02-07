package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.dto.EmbeddingRequest;
import com.backend.reporting.ai.dto.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class OpenAiEmbeddingClient {
    private final WebClient webClient;
  private  String apiKey;

    public OpenAiEmbeddingClient(OpenAiWebClient openAiWebClient,@Value("${openai.api.key}") String apiKey){
        this.webClient = openAiWebClient.client();
        this.apiKey = apiKey;
    }

    public float[] embedding(String text){
        EmbeddingRequest request = new EmbeddingRequest( "text-embedding-3-small", List.of(text));
        System.out.println(request.toString());
        EmbeddingResponse response = webClient.post().uri("/embeddings")
                .headers(h -> {
                    h.setBearerAuth(apiKey);
                    h.setContentType(MediaType.APPLICATION_JSON);
                })
                                 .bodyValue(request)
                                 .retrieve()
                                 .bodyToMono(EmbeddingResponse.class)
                                 .block();
        return response.getData()
                .get(0)
                .getEmbedding();

    }
}
