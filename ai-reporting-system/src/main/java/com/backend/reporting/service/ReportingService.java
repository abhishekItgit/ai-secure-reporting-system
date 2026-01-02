package com.backend.reporting.service;

import com.backend.reporting.dto.ReportResponse;
import com.backend.reporting.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingService {

    private final ReportRepository reportRepository;

    public ReportingService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<ReportResponse> revenueByRegion() {
        return reportRepository.revenueByRegion();
    }

    public List<ReportResponse> orderCountByStatus() {
        return reportRepository.orderCountByStatus();
    }

    public List<ReportResponse> paymentCountByMode() {
        return reportRepository.paymentCountByMode();
    }
}
