package com.backend.reporting.service;

import java.util.List;
import java.util.Map;

public interface ReportExecutionService {
    List<Map<String, Object>> executeReadOnly(String sql);
}
