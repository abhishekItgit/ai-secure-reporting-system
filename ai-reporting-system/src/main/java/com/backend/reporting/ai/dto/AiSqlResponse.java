package com.backend.reporting.ai.dto;

public class AiSqlResponse {

    private String sql;

    public AiSqlResponse(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
