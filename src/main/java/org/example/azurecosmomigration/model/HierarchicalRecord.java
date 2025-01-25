package org.example.azurecosmomigration.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "hierarchicalContainer", partitionKeyPath = "/keyA,/keyB,/keyC")
@Builder
public class HierarchicalRecord {
    @Id
    private String id;
    private String sourceSystem;
    private String dataPayload;
    private long timestamp;

    private String keyA;
    private String keyB;
    private String keyC;

    private static String determinePartitionKey(String sourceSystem, long timestamp) {
        return sourceSystem;
    }
}