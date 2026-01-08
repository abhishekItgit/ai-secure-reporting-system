package com.backend.reporting.ai.dto;

public class AiSqlRequest {

    private String userRequest;
    private String databaseSchema;
    private String conversationMemory;

    public String getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(String userRequest) {
        this.userRequest = userRequest;
    }

    public String getDatabaseSchema() {
        return databaseSchema;
    }

    public void setDatabaseSchema(String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    public String getConversationMemory() {
        return conversationMemory;
    }

    public void setConversationMemory(String conversationMemory) {
        this.conversationMemory = conversationMemory;
    }
}

