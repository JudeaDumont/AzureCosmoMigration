package org.example.azurecosmomigration.service;

import com.azure.cosmos.models.SqlParameter;
import com.azure.cosmos.models.SqlQuerySpec;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.core.query.CosmosQuery;
import com.azure.spring.data.cosmos.core.query.Criteria;
import com.azure.spring.data.cosmos.core.query.CriteriaType;
import org.example.azurecosmomigration.model.MigrationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class SensorService {

    @Value("${spring.cloud.azure.cosmos.database}")
    private String database;

    private final CosmosTemplate cosmosTemplate;

    @Autowired
    public SensorService(CosmosTemplate cosmosTemplate) {
        this.cosmosTemplate = cosmosTemplate;
    }

    public List<MigrationRecord> getHighValueSensors() {

        Criteria criteria = Criteria.getInstance(CriteriaType.GREATER_THAN, "timestamp", List.of(1705675100000L), Part.IgnoreCaseType.NEVER);

        CosmosQuery query = new CosmosQuery(criteria);

        System.out.println("database: " + database);
        Iterable<MigrationRecord> queryResult = cosmosTemplate.find(query, MigrationRecord.class, "bigDataContainer");
        List<MigrationRecord> collect = StreamSupport.stream(queryResult.spliterator(), false)
                .collect(Collectors.toList());
        return collect;
    }

    public List<MigrationRecord> executeRawQuery() {
        String sqlQuery = "SELECT * FROM c WHERE c.timestamp > @timestamp";

        SqlQuerySpec querySpec = new SqlQuerySpec(sqlQuery)
                .setParameters(List.of(new SqlParameter("@timestamp", 1705675100000L)));

        Iterable<MigrationRecord> queryResult = cosmosTemplate.runQuery(querySpec, MigrationRecord.class, MigrationRecord.class);

        return StreamSupport.stream(queryResult.spliterator(), false)
                .collect(Collectors.toList());
    }
}