package com.backend.reporting.ai.schema;

import com.backend.reporting.ai.dao.MetadataRepository;
import com.backend.reporting.ai.model.ColumnMetadata;
import com.backend.reporting.ai.model.SchemaContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Component
public class SchemaContextBuilder {

    private static final String LINE_SEPARATOR  = "/n";
    private final MetadataRepository metadataRepository;

    public SchemaContextBuilder(MetadataRepository metadataRepository){
        this.metadataRepository = metadataRepository;
    }
    public SchemaContext build (String tableName, List<ColumnMetadata> columns){

        StringBuilder sb = new StringBuilder(512);


        appendTableName(sb, tableName);
        appendOptionalPurpose(sb, tableName);
        appendColumns(sb, columns);
        appendRelationships(sb, columns);
        appendNotes(sb);

        return new SchemaContext(tableName,sb.toString());

    }
    private  void   appendTableName( StringBuilder sb,String tableName){
            sb.append("TableName :")
            .append(tableName)
            .append(LINE_SEPARATOR)
            .append(LINE_SEPARATOR);
    }

    private void appendColumns(StringBuilder sb,List<ColumnMetadata> columns){
        sb.append("Column:").append(LINE_SEPARATOR);
        columns.stream()
                .sorted(Comparator
                        .comparing(ColumnMetadata::getPrimaryKey).reversed()
                        .thenComparing(ColumnMetadata::getColumnName))
                .forEach(column -> sb.append(formatColumn(column)));

        sb.append(LINE_SEPARATOR);        ;

    }

    public void appendOptionalPurpose(StringBuilder sb, String  tableName){
        metadataRepository.fetchPurpose(tableName)
                .ifPresent(purpose -> {
                    sb.append("Purpose:")
                            .append(LINE_SEPARATOR)
                            .append(purpose)
                            .append(LINE_SEPARATOR)
                            .append(LINE_SEPARATOR);
                });


    };
    public void appendRelationships(StringBuilder sb, List<ColumnMetadata>columns){
          sb.append("relationship:").append(LINE_SEPARATOR);

          columns.stream().filter(this :: isForeignKeyCandidate)
                  .sorted(Comparator.comparing(ColumnMetadata::getColumnName))
                  .forEach(column -> sb.append(formatRelationship(column)));
          sb.append(LINE_SEPARATOR);

    };
    private String formatRelationship(ColumnMetadata column) {

        String referencedEntity =
                column.getColumnName().substring(0, column.getColumnName().length() - 2);

        return "- " + column.getColumnName() +
                " → " + referencedEntity +
                "." + column.getColumnName() +
                LINE_SEPARATOR;
    }

    private boolean isForeignKeyCandidate(ColumnMetadata column) {
        return !column.getPrimaryKey()
                && column.getColumnName().endsWith("ID");
    }
    private void appendNotes(StringBuilder sb) {
        sb.append("Notes:")
                .append(LINE_SEPARATOR)
                .append("Schema metadata generated deterministically from database structure.");
    }
    private String formatColumn(ColumnMetadata column) {

        StringBuilder line = new StringBuilder();

        line.append("- ")
                .append(column.getColumnName())
                .append(" (")
                .append(column.getDataType());

        if (column.getPrimaryKey()) {
            line.append(", primary key");
        }
        if (!column.getNullable()) {
            line.append(", not null");
        }
        line.append("): ")
                .append(deriveColumnMeaning(column.getColumnName()))
                .append(LINE_SEPARATOR);

        return line.toString();
    }
    private String deriveColumnMeaning(String columnName) {

        String col = columnName.toLowerCase(Locale.ROOT);

        if (col.endsWith("id")) {
            return "Reference identifier";
        }
        if (col.contains("amount")) {
            return "Monetary amount";
        }
        if (col.contains("status")) {
            return "Status indicator";
        }
        if (col.contains("name")) {
            return "Name field";
        }
        if (col.contains("date") || col.contains("time")) {
            return "Timestamp";
        }

        return "Business attribute";
    }
    private void validate(String tableName, List<ColumnMetadata> columns) {

        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("tableName must not be blank");
        }

        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException(
                    "No column metadata found for table: " + tableName
            );
        }
    }
}
