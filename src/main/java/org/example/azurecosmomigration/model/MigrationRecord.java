package org.example.azurecosmomigration.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "bigDataContainer")
public class MigrationRecord {
    @Id
    private String id;
    private String sourceSystem;
    private String dataPayload;
    private long timestamp;
}