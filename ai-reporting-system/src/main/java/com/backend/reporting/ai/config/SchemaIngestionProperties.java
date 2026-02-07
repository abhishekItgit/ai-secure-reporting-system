package com.backend.reporting.ai.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "schema.ingestion")
public class SchemaIngestionProperties {
    private List<String> excludeTables;

    public List<String> getExcludeTables(){ return this.excludeTables;}
    public void setExcludeTables(List<String> excludeTables){ this.excludeTables = excludeTables;}
}
