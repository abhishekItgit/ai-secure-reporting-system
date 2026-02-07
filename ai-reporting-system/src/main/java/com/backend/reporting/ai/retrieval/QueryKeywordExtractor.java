package com.backend.reporting.ai.retrieval;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class QueryKeywordExtractor {

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "but", "if", "then", "else", "when",
            "where", "what", "which", "who", "whom", "whose", "how", "is", "are",
            "was", "were", "be", "been", "being", "of", "for", "to", "in", "on",
            "at", "by", "with", "from", "as", "it", "this", "that", "these",
            "those", "show", "list", "get", "give", "fetch", "find", "all"
    );

    public List<String> extract(String question) {
        if (question == null || question.isBlank()) {
            return List.of();
        }

        String normalized = question
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s_]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        String[] tokens = normalized.split(" ");
        Set<String> keywords = new LinkedHashSet<>();
        for (String token : tokens) {
            if (token.length() < 3 || STOP_WORDS.contains(token)) {
                continue;
            }
            keywords.add(token);
        }

        return new ArrayList<>(keywords);
    }
}
