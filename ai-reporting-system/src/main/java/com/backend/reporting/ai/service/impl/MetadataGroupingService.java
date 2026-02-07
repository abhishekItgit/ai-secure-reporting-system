package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.model.ColumnMetadata;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetadataGroupingService {
    Map<String , List<ColumnMetadata>> groupMetaData(List<ColumnMetadata> metadata){
        return metadata.stream().collect(Collectors.groupingBy(r->r.getTableName()));
    }
}
