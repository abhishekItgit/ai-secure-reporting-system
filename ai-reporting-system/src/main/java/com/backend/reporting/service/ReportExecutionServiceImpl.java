package com.backend.reporting.service;

import com.backend.reporting.ai.security.SqlSafetyValidator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportExecutionServiceImpl implements ReportExecutionService {
    private final JdbcTemplate jdbcTemplate;
    private final SqlSafetyValidator sqlSafetyValidator;

    public ReportExecutionServiceImpl(
            JdbcTemplate jdbcTemplate,
            SqlSafetyValidator sqlSafetyValidator
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlSafetyValidator = sqlSafetyValidator;
    }

    @Override
    public List<Map<String, Object>> executeReadOnly(String sql) {
        sqlSafetyValidator.validate(sql);

        return jdbcTemplate.queryForList(sql);
    }
}


