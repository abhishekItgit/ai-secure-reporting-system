package com.backend.reporting.ai;

import org.springframework.stereotype.Component;

@Component
public class IntentValidator {

    public void validate(ReportIntent intent) {
        if (intent.getReportType() == null) {
            throw new IllegalArgumentException("Unsupported report");
        }
    }
}

