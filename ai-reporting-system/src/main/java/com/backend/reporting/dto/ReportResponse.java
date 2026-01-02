package com.backend.reporting.dto;

public class ReportResponse {

    private String label;
    private Object value;

    public ReportResponse(String label, Object value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return value;
    }
}
