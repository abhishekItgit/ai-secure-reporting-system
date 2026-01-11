package com.backend.reporting.ai.cache;

import com.backend.reporting.ai.schema.SchemaProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaConfig {

    @Bean
    public SchemaHashProvider schemaHashProvider(
            SchemaProvider schemaProvider
    ) {
        return new SchemaHashProvider(schemaProvider.getSchema());
    }
}
