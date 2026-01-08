package com.backend.reporting.ai.service;

public interface IOpenAiClient {
    String complete(String prompt, String model);

}
