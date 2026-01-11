package com.backend.reporting.ai.cache;




import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SqlCacheKeyBuilder {

    private SqlCacheKeyBuilder() {}

    public static String build(String schemaHash, String normalizedQuery) {
        return schemaHash + ":" + sha256(normalizedQuery).substring(0, 10);
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash query", e);
        }
    }
}
