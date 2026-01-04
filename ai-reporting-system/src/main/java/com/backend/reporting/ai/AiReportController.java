package com.backend.reporting.ai;


import com.backend.reporting.dto.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/reports")
public class AiReportController {

    private final OpenAiClient openAiClient;
    private final ReportIntentResolver resolver;

    public AiReportController(OpenAiClient openAiClient,
                              ReportIntentResolver resolver) {
        this.openAiClient = openAiClient;
        this.resolver = resolver;
    }

    @PostMapping
    public ApiResponse<?> generateReport(@RequestBody String question) {

        ReportIntent intent = openAiClient.getIntent(question);
        return ApiResponse.success(resolver.resolve(intent));
    }
}
