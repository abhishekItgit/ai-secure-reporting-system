package com.backend.reporting.ai.service;

public interface AiSqlService {

    String generateSql(
          //  String databaseSchema,
            String userRequest,
            String conversationMemory
    );
}

