package org.example.azurecosmomigration.repo;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.example.azurecosmomigration.model.HierarchicalRecord;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HierarchicalRepository extends CosmosRepository<HierarchicalRecord, String> {

    @Query("SELECT * FROM c WHERE c.timestamp > @timeStamp")
    List<HierarchicalRecord> findAfterTimeStamp(@Param("timeStamp") double timeStamp);

    @Query("SELECT * FROM c WHERE c.keyA = '1'")
    List<HierarchicalRecord> HierarchicalKeyQuery();
}