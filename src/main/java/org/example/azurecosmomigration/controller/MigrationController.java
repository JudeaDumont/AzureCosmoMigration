package org.example.azurecosmomigration.controller;

import org.example.azurecosmomigration.model.HierarchicalRecord;
import org.example.azurecosmomigration.model.MigrationRecord;
import org.example.azurecosmomigration.repo.HierarchicalRepository;
import org.example.azurecosmomigration.repo.MigrationRepository;
import org.example.azurecosmomigration.service.CosmosMigrationService;
import org.example.azurecosmomigration.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/migration")
public class MigrationController {

    private final MigrationRepository migrationRepository;
    private final HierarchicalRepository hierarchicalRepository;
    private final SensorService sensorService;
    private final CosmosMigrationService cosmosMigrationService;

    @Autowired
    public MigrationController(MigrationRepository migrationRepository,
                               SensorService sensorService,
                               CosmosMigrationService cosmosMigrationService,
                               HierarchicalRepository hierarchicalRepository) {
        this.migrationRepository = migrationRepository;
        this.hierarchicalRepository = hierarchicalRepository;
        this.sensorService = sensorService;
        this.cosmosMigrationService = cosmosMigrationService;
    }

    @PostMapping("/insert")
    public MigrationRecord insertRecord(@RequestBody MigrationRecord record) {
        return migrationRepository.save(record);
    }

    @PostMapping("/insertHierarchical")
    public HierarchicalRecord insertHierarchical(@RequestBody HierarchicalRecord record) {
        return hierarchicalRepository.save(record);
    }


    @GetMapping("/compositeKeyQuery")
    public ResponseEntity<List<MigrationRecord>> compositeKeyQuery() {
        return ResponseEntity.ok(migrationRepository.compositeKeyQuery());
    }

    @GetMapping("/all")
    public ResponseEntity<List<MigrationRecord>> getAllRecords() {
        Iterable<MigrationRecord> recordsIterable = migrationRepository.findAll();
        List<MigrationRecord> recordsList = StreamSupport
                .stream(recordsIterable.spliterator(), false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(recordsList);
    }
    @GetMapping("/cosmosTemplateQuery")
    public ResponseEntity<List<MigrationRecord>> cosmosTemplateQuery() {
        return ResponseEntity.ok(sensorService.getHighValueSensors());
    }
    @GetMapping("/executeRawQuery")
    public ResponseEntity<List<MigrationRecord>> executeRawQuery() {
        return ResponseEntity.ok(sensorService.executeRawQuery());
    }
    @GetMapping("/findAfterTimeStamp")
    public ResponseEntity<List<MigrationRecord>> findAfterTimeStamp(@RequestParam long timestamp) {
        return ResponseEntity.ok(migrationRepository.findAfterTimeStamp(timestamp));
    }
    @GetMapping("/runMigration")
    public ResponseEntity<String> runMigration() {
        cosmosMigrationService.runMigration();
        return ResponseEntity.ok("Success");
    }
}