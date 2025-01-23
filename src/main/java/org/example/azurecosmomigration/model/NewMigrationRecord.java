package org.example.azurecosmomigration.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "newBigDataContainer")
@Builder
public class NewMigrationRecord {
    @Id
    private String id;
    private String sourceSystem;
    private String dataPayload;
    private long timestamp;

    @PartitionKey
    private String partitionKey;

    private static String determinePartitionKey(String sourceSystem, long timestamp) {
        return sourceSystem;
    }
}