package com.backend.reporting.ai.security;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;

@Component
public class SqlSafetyValidator {

    private static final Set<String> FORBIDDEN_KEYWORDS = Set.of(
            "insert", "update", "delete", "drop",
            "alter", "truncate", "create", "merge",
            "call", "grant", "revoke", "commit", "rollback"
    );

    private static final Set<String> FORBIDDEN_PATTERNS = Set.of(
            "--", "/*", "*/", ";",
            "information_schema",
            "mysql.", "sys.", "performance_schema"
    );

    public void validate(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException("SQL query is empty");
        }

        String normalized = sql.toLowerCase(Locale.ROOT).trim();

        if (normalized.contains("not_enough_information")) {
            throw new SecurityException("Insufficient information to generate SQL");
        }

        if (!normalized.startsWith("select")) {
            throw new SecurityException("Only SELECT queries are allowed");
        }

        FORBIDDEN_KEYWORDS.forEach(keyword -> {
            if (normalized.contains(keyword + " ")) {
                throw new SecurityException(
                        "Forbidden SQL keyword detected: " + keyword
                );
            }
        });

        FORBIDDEN_PATTERNS.forEach(pattern -> {
            if (normalized.contains(pattern)) {
                throw new SecurityException(
                        "Forbidden SQL pattern detected: " + pattern
                );
            }
        });
    }
}
