package com.backend.reporting.ai.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class PostgresDataSourceConfig {


    @Bean(name = "pgDataSource")
    public DataSource pgDataSource(
            @Value("${pg.datasource.url}") String url,
            @Value("${pg.datasource.username}") String username,
            @Value("${pg.datasource.password}") String password
    ) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "pgJdbcTemplate")
    public JdbcTemplate pgJdbcTemplate(
            @Qualifier("pgDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
