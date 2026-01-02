package com.backend.reporting.controller;


import com.backend.reporting.dto.ApiResponse;
import com.backend.reporting.dto.ReportResponse;
import com.backend.reporting.service.ReportingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/revenue-by-region")
    public ApiResponse<List<ReportResponse>> revenueByRegion() {
        return ApiResponse.success(reportingService.revenueByRegion());
    }

    @GetMapping("/order-count-by-status")
    public ApiResponse<List<ReportResponse>> orderCountByStatus() {
        return ApiResponse.success(reportingService.orderCountByStatus());
    }

    @GetMapping("/payment-count-by-mode")
    public ApiResponse<List<ReportResponse>> paymentCountByMode() {
        return ApiResponse.success(reportingService.paymentCountByMode());
    }
}
