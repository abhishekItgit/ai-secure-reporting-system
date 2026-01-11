package com.backend.reporting.redis.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiContext {

    private String lastIntent;
    private List<String> tablesUsed;
    private String timeFilter;
    private String groupBy;
    private double confidence;
}
