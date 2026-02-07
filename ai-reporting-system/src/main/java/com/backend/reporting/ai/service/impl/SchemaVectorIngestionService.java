package com.backend.reporting.ai.service.impl;



import com.backend.reporting.ai.dao.SchemaVectorRepository;
import com.backend.reporting.ai.model.SchemaContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class SchemaVectorIngestionService {

    private final SchemaContextService schemaContextService;
    private final OpenAiEmbeddingClient embeddingClient;
    private final SchemaVectorRepository vectorRepository;

    public SchemaVectorIngestionService(
            SchemaContextService schemaContextService,
            OpenAiEmbeddingClient embeddingClient,
            SchemaVectorRepository vectorRepository
    ) {
        this.schemaContextService = schemaContextService;
        this.embeddingClient = embeddingClient;
        this.vectorRepository = vectorRepository;
    }
    @Async
    public void ingestAsync() throws InterruptedException {
        ingestInternal();
    }

    public void ingestInternal() throws InterruptedException {
        List<SchemaContext> contexts =
                schemaContextService.buildSchemaContexts(" ");
        for (SchemaContext context : contexts) {
        try {

        //  Generate embedding
        float[] embedding =
                embeddingClient.embedding(context.getContent());

        //  Deterministic UUID (per table)
        UUID id = UUID.nameUUIDFromBytes(
                context.getTableName()
                        .getBytes(StandardCharsets.UTF_8)
        );

        //  Store in pgvector
        vectorRepository.upsert(
                id,
                context.getTableName(),
                context.getContent(),
                embedding
        );
        Thread.sleep(6000);

  } catch (WebClientResponseException.TooManyRequests e) {
         handleRateLimit(e);
} catch (Exception e) {
   System.out.println("Failed chunk :"+ context.getTableName()+ e);
}
        }

    }
    private void handleRateLimit(WebClientResponseException.TooManyRequests e) {
        try {
            System.out.println("Failed: " + e);
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
            System.out.println("Failed: " + e);
        }



    }
}
