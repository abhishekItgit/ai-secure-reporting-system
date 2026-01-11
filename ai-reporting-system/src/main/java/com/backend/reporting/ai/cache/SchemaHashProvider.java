package com.backend.reporting.ai.cache;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SchemaHashProvider {

    private final String schemaHash;

    public SchemaHashProvider(String schemaText) {
        this.schemaHash = sha256(schemaText).substring(0, 6);
    }

    public String getSchemaHash() {
        return schemaHash;
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash schema", e);
        }
    }
}
