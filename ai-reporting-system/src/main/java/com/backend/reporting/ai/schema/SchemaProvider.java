package com.backend.reporting.ai.schema;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SchemaProvider {

    private String schema;

    @PostConstruct
    public void load() throws IOException {
        schema = new String(
                getClass().getClassLoader()
                        .getResourceAsStream("schema/ai-reporting-schema.txt")
                        .readAllBytes()
        );
    }

    public String getSchema() {
        return schema;
    }
}


