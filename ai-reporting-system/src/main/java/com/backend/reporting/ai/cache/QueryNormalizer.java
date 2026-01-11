package com.backend.reporting.ai.cache;

public class QueryNormalizer {

    private QueryNormalizer() {}

    public static String normalize(String input) {
        if (input == null) return "";
        return input
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }
}
