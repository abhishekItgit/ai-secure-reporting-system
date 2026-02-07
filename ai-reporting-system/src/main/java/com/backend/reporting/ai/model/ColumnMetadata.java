package com.backend.reporting.ai.model;

public class ColumnMetadata {
    private  String tableName;
    private String ColumnName;
    private String dataType;
    private Boolean nullable;
    private Boolean primaryKey;

    public ColumnMetadata(){}
    public ColumnMetadata(String tableName, String columnName, String dataType,Boolean nullable, Boolean primaryKey ){
       this.tableName = tableName;
        this.ColumnName = columnName;
        this.dataType = dataType;
        this.nullable = nullable;
        this.primaryKey = primaryKey;

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String columnName) {
        ColumnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
