package com.backend.reporting.ai.model;

public class SchemaContext {
    private String tableName;
    private String content;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SchemaContext(String tableName, String content) {
        this.tableName = tableName;
        this.content = content;
    }

    public SchemaContext() {
    }
}
