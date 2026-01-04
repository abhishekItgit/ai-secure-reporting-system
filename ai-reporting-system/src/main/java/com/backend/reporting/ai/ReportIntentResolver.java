package com.backend.reporting.ai;

import com.backend.reporting.dto.ReportResponse;
import com.backend.reporting.service.ReportingService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportIntentResolver {

    private final ReportingService reportingService;

    public ReportIntentResolver(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    public List<ReportResponse> resolve(ReportIntent intent) {

        return switch (intent.getReportType()) {

            case REVENUE_BY_REGION ->
                    reportingService.revenueByRegionLast30Days();

            case ORDER_COUNT_BY_STATUS ->
                    reportingService.orderCountByStatus();

            case PAYMENT_COUNT_BY_MODE ->
                    reportingService.paymentCountByMode();
        };
    }
}
