package org.example.azurecosmomigration.repo;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.example.azurecosmomigration.model.MigrationRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MigrationRepository extends CosmosRepository<MigrationRecord, String> {

    @Query("SELECT c.id, c.sensorName, c.sensorValue FROM c WHERE c.sensorValue > 900")
    List<MigrationRecord> findHighValueSensors(double sensorValue);
}