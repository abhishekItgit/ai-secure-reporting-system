package com.backend.reporting.ai.prompt;

import com.backend.reporting.ai.dto.SchemaContextChunk;
import com.backend.reporting.ai.model.SchemaContextFormatter;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RagPromptBuilder {

    private final SchemaContextFormatter formatter;

    public RagPromptBuilder(SchemaContextFormatter formatter) {
        this.formatter = formatter;
    }

    public String buildPrompt(
            String question,
            List<SchemaContextChunk> schemaChunks
    ) {

        String schemaContext = formatter.format(schemaChunks);

        return """
            You are a senior database engineer.

            Your task is to generate a single MySQL SELECT query based on the user question.

            User Question:
            %s

            Available Tables:
            %s

            Rules:
            - Use ONLY the tables listed above.
            - Do NOT invent tables or columns.
            - Do NOT use subqueries that reference unknown tables.
            - Do NOT generate INSERT, UPDATE, DELETE, DROP, or ALTER statements.
            - Output ONLY the SQL query.
            - If the question cannot be answered using the available tables, respond with:
              -- NOT_ENOUGH_INFORMATION
            """.formatted(question, schemaContext);
    }

}
