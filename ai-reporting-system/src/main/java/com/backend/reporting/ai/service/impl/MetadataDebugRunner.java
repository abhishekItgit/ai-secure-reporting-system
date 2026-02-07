package com.backend.reporting.ai.service.impl;

import com.backend.reporting.ai.model.ColumnMetadata;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Map;


@Configuration
public class MetadataDebugRunner implements CommandLineRunner {

 private final  MetadataService metadataService;
 private final  MetadataGroupingService metadataGroupingService;
    private final SchemaContextService service;
    private final SchemaVectorIngestionService schemaVectorIngestionService;

 public MetadataDebugRunner(MetadataService metadataService,MetadataGroupingService metadataGroupingService,SchemaContextService schemaContextService,SchemaVectorIngestionService schemaVectorIngestionService){
     this.metadataService = metadataService;
     this.metadataGroupingService = metadataGroupingService;
     this.service = schemaContextService;
     this.schemaVectorIngestionService = schemaVectorIngestionService;
 }

    @Override
    public void run(String... args) throws Exception {
     List<ColumnMetadata>  columnMetadata = metadataService.loadMetaData(null);
        Map<String,List<ColumnMetadata>> gp =  metadataGroupingService.groupMetaData(columnMetadata);

     columnMetadata.forEach(r -> System.out.println(r.getTableName() + "|" + r.getColumnName() +"|"
                                +  r.getDataType() + "|" +  r.getNullable() + "|"+ r.getPrimaryKey()));

     for(Map.Entry<String,List<ColumnMetadata>> map : gp.entrySet()){
         String key = map.getKey();
         List<ColumnMetadata> val = map.getValue();
         System.out.println(key + ":" + val);
     }
        service.buildSchemaContexts("dmihfcdemo")
                .forEach(ctx -> {
                    System.out.println("================================");
                    System.out.println(ctx.getContent());
                });

    }

}
