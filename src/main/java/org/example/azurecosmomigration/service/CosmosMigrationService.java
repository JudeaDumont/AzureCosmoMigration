package org.example.azurecosmomigration.service;

import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.SqlQuerySpec;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.repository.support.CosmosEntityInformation;
import org.example.azurecosmomigration.model.MigrationRecord;
import org.example.azurecosmomigration.model.NewMigrationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CosmosMigrationService {

    private final CosmosTemplate cosmosTemplate;

    @Autowired
    public CosmosMigrationService(
            CosmosTemplate cosmosTemplate) {
        this.cosmosTemplate = cosmosTemplate;
    }

    // 🚀 Step 1: Extract data from old container
    public List<MigrationRecord> extractData() {
        List<MigrationRecord> records = new ArrayList<>();
        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();

        String sqlQuery = "SELECT * FROM c";

        SqlQuerySpec querySpec = new SqlQuerySpec(sqlQuery);

        Iterable<MigrationRecord> queryResult = cosmosTemplate.runQuery(querySpec, MigrationRecord.class, MigrationRecord.class);

        for (MigrationRecord migrationRecord : queryResult) {
            records.add(migrationRecord);
        }
        System.out.println("Extracted " + records.size() + " records from old container.");
        return records;
    }

    // 🚀 Step 2: Transform Data (Add partitionKey if needed)
    public List<NewMigrationRecord> transformData(List<MigrationRecord> oldRecords, String partitionKey) {
        List<NewMigrationRecord> transformedRecords = new ArrayList<>();
        for (MigrationRecord record : oldRecords) {
            NewMigrationRecord newRecord = NewMigrationRecord.builder()
                    .id(record.getId())
                    .sourceSystem(record.getSourceSystem())
                    .partitionKey(partitionKey)
                    .dataPayload(record.getDataPayload())
                    .timestamp(record.getTimestamp()).build();
            transformedRecords.add(newRecord);
        }
        System.out.println("Transformed " + transformedRecords.size() + " records.");
        return transformedRecords;
    }

    // 🚀 Step 3: Bulk Insert into new container
    public void bulkInsert(List<NewMigrationRecord> records, String partitionKey) {

        CosmosEntityInformation<NewMigrationRecord, String> entityInfo =
                new CosmosEntityInformation<>(NewMigrationRecord.class);

        cosmosTemplate.insertAll(entityInfo, records);

        System.out.println("Bulk Migration Completed: " + records.size() + " records inserted.");
    }

    // 🚀 Main Migration Function
    public void runMigration() {
        List<MigrationRecord> extractedData = extractData();
        List<NewMigrationRecord> transformedData = transformData(extractedData, "legacy-misc");
        bulkInsert(transformedData, "legacy-misc");
    }
}
