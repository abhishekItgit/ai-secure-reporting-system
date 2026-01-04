package com.backend.reporting.ai;


public class ReportIntent {

    private ReportType reportType;
    private String timeRange;

    public ReportType getReportType() {
        return reportType;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }
}
