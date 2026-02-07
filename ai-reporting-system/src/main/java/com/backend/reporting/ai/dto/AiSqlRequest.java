package com.backend.reporting.ai.dto;

import jakarta.validation.constraints.NotBlank;

public class AiSqlRequest {

    @NotBlank
    private String userRequest;
    private String conversationMemory;

    public String getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(String userRequest) {
        this.userRequest = userRequest;
    }

    public String getConversationMemory() {
        return conversationMemory;
    }

    public void setConversationMemory(String conversationMemory) {
        this.conversationMemory = conversationMemory;
    }
}
