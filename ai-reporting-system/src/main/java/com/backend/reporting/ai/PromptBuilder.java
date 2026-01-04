package com.backend.reporting.ai;

public class PromptBuilder {

    public static String build(String userQuery) {
        return """
        You are an AI assistant for a backend reporting system.

        Convert the user question into STRICT JSON only.

        Allowed reportType values:
        - REVENUE_BY_REGION
        - ORDER_COUNT_BY_STATUS
        - PAYMENT_COUNT_BY_MODE

        Allowed timeRange values:
        - LAST_7_DAYS
        - LAST_30_DAYS
        - ALL_TIME

        Response format:
        {
          "reportType": "REVENUE_BY_REGION",
          "timeRange": "LAST_30_DAYS"
        }

        Do NOT add explanation.
        Do NOT add extra fields.

        User question:
        "%s"
        """.formatted(userQuery);
    }
}
