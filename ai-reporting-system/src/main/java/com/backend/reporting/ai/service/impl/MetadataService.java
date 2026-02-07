package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.dao.MetadataRepository;
import com.backend.reporting.ai.model.SchemaContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetadataService {

    private final MetadataRepository metadataRepository;

    public MetadataService(MetadataRepository metadataRepository){
        this.metadataRepository = metadataRepository;
    }

    public List<SchemaContext> fetchContext(){
        return metadataRepository.fetchSchemaContexts();
    }
}
