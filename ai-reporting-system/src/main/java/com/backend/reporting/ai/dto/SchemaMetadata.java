package com.backend.reporting.ai.dto;

public record SchemaMetadata( String schema,
                              String table,
                              String chunkType,
                              String source,
                              int version) {
}
