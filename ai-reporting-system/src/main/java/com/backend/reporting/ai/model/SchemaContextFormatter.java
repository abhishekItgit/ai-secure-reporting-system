package com.backend.reporting.ai.model;


import com.backend.reporting.ai.dto.SchemaContextChunk;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchemaContextFormatter {

    public String format(List<SchemaContextChunk> chunks) {

        StringBuilder sb = new StringBuilder();

        for (SchemaContextChunk chunk : chunks) {
            String table = (String) chunk.metadata().get("table");

            sb.append("Table: ").append(table).append("\n");
            sb.append("Description:\n");
            sb.append(chunk.content()).append("\n\n");
        }

        return sb.toString();
    }
}
