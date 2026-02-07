package com.backend.reporting.ai.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class PostgresDataSourceConfig {


    @Bean(name = "pgDataSource")
    public DataSource pgDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://3.110.191.142:5433/vectordb")
                .username("vector_user")
                .password("vector_user")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "pgJdbcTemplate")
    public JdbcTemplate pgJdbcTemplate(
            @Qualifier("pgDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

