package org.example.azurecosmomigration.repo;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.example.azurecosmomigration.model.MigrationRecord;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MigrationRepository extends CosmosRepository<MigrationRecord, String> {

    @Query("SELECT * FROM c WHERE c.timestamp > @timeStamp")
    List<MigrationRecord> findAfterTimeStamp(@Param("timeStamp") double timeStamp);

    @Query("SELECT * FROM c WHERE STARTSWITH(c.partitionKey, '1-')")
    List<MigrationRecord> compositeKeyQuery();
}