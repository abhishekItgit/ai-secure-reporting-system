package com.backend.reporting.ai.controller;


import com.backend.reporting.ai.dto.AiSqlRequest;
import com.backend.reporting.ai.dto.AiSqlResponse;
import com.backend.reporting.ai.prompt.RagPromptBuilder;
import com.backend.reporting.ai.service.AiSqlService;
import com.backend.reporting.ai.service.impl.*;
import com.backend.reporting.service.ReportExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/reports/ai")
public class AiSqlController {

    private final AiSqlService aiSqlService;
    private final ReportExecutionService reportExecutionService;
    private final AiOrchestratorService aiOrchestratorService;
    private final SchemaVectorIngestionService schemaVectorIngestionService;

    private final RagPromptService ragPromptService;
    private final SqlGenerationService sqlGenerationService;
    private final SchemaContextService schemaContextService;



    public AiSqlController(
            AiSqlService aiSqlService,
            ReportExecutionService reportExecutionService,
            AiOrchestratorService  aiOrchestratorService,
            RagPromptService ragPromptService,
            SqlGenerationService sqlGenerationServic,
            SchemaVectorIngestionService schemaVectorIngestionService,
            SchemaContextService schemaContextService
    ) {
        this.aiSqlService = aiSqlService;
        this.reportExecutionService = reportExecutionService;
        this.aiOrchestratorService = aiOrchestratorService;
        this.schemaVectorIngestionService = schemaVectorIngestionService;
        this.ragPromptService = ragPromptService;
        this.sqlGenerationService = sqlGenerationServic;
        this.schemaContextService = schemaContextService;
    }


    @PostMapping("/sql")
    public ResponseEntity<AiSqlResponse> generateSql(@RequestBody AiSqlRequest request) {
        System.out.println(">>> AI SQL CONTROLLER HIT <<<");
        String sql = aiSqlService.generateSql(
            //    request.getDatabaseSchema(),
                request.getUserRequest(),
                request.getConversationMemory());

        return ResponseEntity.ok(new AiSqlResponse(sql));
    }
    @PostMapping("/execute")
    public ResponseEntity<?> executeReport(
            @RequestBody AiSqlResponse request
    ) {
        return ResponseEntity.ok(
                reportExecutionService.executeReadOnly(request.getSql())
        );
    }
    @PostMapping("/generate")
    public ResponseEntity<String> generateSql(
            @RequestParam("question") String userQuestion
    ) {
        String sql = aiOrchestratorService.process(userQuestion);
        return ResponseEntity.ok(sql);
    }
    @PostMapping("/ingest/schema")
    public ResponseEntity<String> ingest() throws InterruptedException {
        schemaVectorIngestionService.ingestAsync();
        return ResponseEntity.ok("Schema embeddings ingested");
    }


    @PostMapping("/generate/prompt/sql")
    public ResponseEntity<Map<String, String>> generatePromptSql(
            @RequestBody Map<String, String> request
    ) {

        String question = request.get("question");

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question must not be empty");
        }

      String prompt =  ragPromptService.buildPromptForQuestion(question);

        // Generate SQL using the SAME prompt
        String sql = sqlGenerationService.generateSql(prompt);

        return ResponseEntity.ok(
                Map.of(
                        "prompt", prompt,
                        "sql", sql
                )
        );
    }

}
