package com.backend.reporting.ai.schema;

public class MetadataSql {
    public static final String FETCH_COLUMNS = """
    SELECT
            TABLE_NAME,
            COLUMN_NAME,
            DATA_TYPE,
            IS_NULLABLE,
            COLUMN_KEY
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = ?
    AND TABLE_NAME NOT LIKE 'QRTZ_%'
    AND TABLE_NAME NOT LIKE '%_AUD'
     AND TABLE_NAME NOT LIKE 'da%'
     AND TABLE_NAME NOT LIKE '%backup%'
     AND TABLE_NAME NOT LIKE '%bkp%'
     AND TABLE_NAME NOT LIKE '%stg'
     AND TABLE_NAME NOT LIKE '%restore%'
     AND TABLE_NAME NOT REGEXP '_[0-9]+$'
     AND TABLE_NAME NOT REGEXP '_[0-9]{1,2}[A-Za-z]{3}[0-9]{4}$'
    AND TABLE_NAME NOT IN ('flyway_schema_history','schema_table_purpose')
    ORDER BY TABLE_NAME, ORDINAL_POSITION""";
}
