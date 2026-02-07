package com.backend.reporting.ai.controller;


import com.backend.reporting.ai.dto.AiSqlRequest;
import com.backend.reporting.ai.dto.AiSqlResponse;
import com.backend.reporting.ai.service.AiSqlService;
import com.backend.reporting.ai.service.impl.AiOrchestratorService;
import com.backend.reporting.ai.service.impl.RagPromptService;
import com.backend.reporting.ai.service.impl.SchemaVectorIngestionService;
import com.backend.reporting.ai.service.impl.SqlGenerationService;
import com.backend.reporting.service.ReportExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/reports/ai")
public class AiSqlController {

    private static final Logger logger = LoggerFactory.getLogger(AiSqlController.class);
    private final AiSqlService aiSqlService;
    private final ReportExecutionService reportExecutionService;
    private final AiOrchestratorService aiOrchestratorService;
    private final SchemaVectorIngestionService schemaVectorIngestionService;

    private final RagPromptService ragPromptService;
    private final SqlGenerationService sqlGenerationService;
    public AiSqlController(
            AiSqlService aiSqlService,
            ReportExecutionService reportExecutionService,
            AiOrchestratorService  aiOrchestratorService,
            RagPromptService ragPromptService,
            SqlGenerationService sqlGenerationServic,
            SchemaVectorIngestionService schemaVectorIngestionService
    ) {
        this.aiSqlService = aiSqlService;
        this.reportExecutionService = reportExecutionService;
        this.aiOrchestratorService = aiOrchestratorService;
        this.schemaVectorIngestionService = schemaVectorIngestionService;
        this.ragPromptService = ragPromptService;
        this.sqlGenerationService = sqlGenerationServic;
    }


    @PostMapping("/sql")
    public ResponseEntity<AiSqlResponse> generateSql(@RequestBody @Valid AiSqlRequest request) {
        logger.info("AI SQL generation requested");
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
