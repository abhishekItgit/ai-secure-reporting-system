package com.backend.reporting.ai.controller;


import com.backend.reporting.ai.dto.AiSqlRequest;
import com.backend.reporting.ai.dto.AiSqlResponse;
import com.backend.reporting.ai.service.AiSqlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports/ai")
public class AiSqlController {

    private final AiSqlService aiSqlService;

    public AiSqlController(AiSqlService aiSqlService) {
        this.aiSqlService = aiSqlService;
    }

    @PostMapping("/sql")
    public ResponseEntity<AiSqlResponse> generateSql(
            @RequestBody AiSqlRequest request
    ) {
        String sql = aiSqlService.generateSql(
                request.getDatabaseSchema(),
                request.getUserRequest(),
                request.getConversationMemory()
        );

        return ResponseEntity.ok(new AiSqlResponse(sql));
    }
}
