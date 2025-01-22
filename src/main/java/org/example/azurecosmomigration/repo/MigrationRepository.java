package org.example.azurecosmomigration.repo;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.example.azurecosmomigration.model.MigrationRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface MigrationRepository extends CosmosRepository<MigrationRecord, String> {
}