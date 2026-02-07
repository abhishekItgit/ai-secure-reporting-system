package com.backend.reporting.ai.dao;

import com.backend.reporting.ai.model.ColumnMetadata;
import com.backend.reporting.ai.model.SchemaContext;
import com.backend.reporting.ai.schema.MetadataSql;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MetadataRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String schemaName;

    public MetadataRepository(
            @Qualifier("mysqlJdbcTemplate")   JdbcTemplate jdbcTemplate,
            @Value("${schema.metadata.name}") String schemaName
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.schemaName = schemaName;
    }

    public List<ColumnMetadata> fetchColumnMetaData() {
        return jdbcTemplate.query(
                MetadataSql.FETCH_COLUMNS,
                new Object[]{ schemaName },
                (rs, rowNum) -> {
                    ColumnMetadata columnMetadata = new ColumnMetadata();
                    columnMetadata.setTableName(rs.getString("TABLE_NAME"));
                    columnMetadata.setColumnName(rs.getString("COLUMN_NAME"));
                    columnMetadata.setDataType(rs.getString("DATA_TYPE"));
                    columnMetadata.setNullable(
                            "YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"))
                    );
                    columnMetadata.setPrimaryKey(
                            "PRI".equalsIgnoreCase(rs.getString("COLUMN_KEY"))
                    );
                    return columnMetadata;
                }
        );
    }

    public Optional<String> fetchPurpose(String tableName) {
        try {
            String sql =
                    "SELECT purpose FROM " + schemaName +
                            ".schema_table_purpose WHERE table_name = ?";

            return jdbcTemplate.query(
                    sql,
                    rs -> rs.next()
                            ? Optional.of(rs.getString("purpose"))
                            : Optional.empty(),
                    tableName
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<SchemaContext> fetchSchemaContexts() {

        String sql = """
            SELECT 
                table_name,
                purpose
            FROM schema_table_purpose where table_name = 'tblLoanApplicationDisbursalDetail'
            """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new SchemaContext(
                                rs.getString("table_name"),
                                rs.getString("purpose")
                        )
        );
    }
}

