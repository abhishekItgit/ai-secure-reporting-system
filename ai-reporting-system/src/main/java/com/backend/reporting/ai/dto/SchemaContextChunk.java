package com.backend.reporting.ai.dto;

import java.util.Map;

public record SchemaContextChunk(String content,
                                 Map<String, Object> metadata) {
}
