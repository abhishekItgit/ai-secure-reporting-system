package com.backend.reporting.ai.controller;


import com.backend.reporting.ai.dto.AiSqlRequest;
import com.backend.reporting.ai.dto.AiSqlResponse;
import com.backend.reporting.ai.service.AiSqlService;
import com.backend.reporting.service.ReportExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reports/ai")
public class AiSqlController {

    private final AiSqlService aiSqlService;
    private final ReportExecutionService reportExecutionService;

    public AiSqlController(
            AiSqlService aiSqlService,
            ReportExecutionService reportExecutionService
    ) {
        this.aiSqlService = aiSqlService;
        this.reportExecutionService = reportExecutionService;
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

}
