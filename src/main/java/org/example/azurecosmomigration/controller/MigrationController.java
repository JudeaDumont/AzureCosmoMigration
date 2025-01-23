package org.example.azurecosmomigration.controller;

import org.example.azurecosmomigration.model.MigrationRecord;
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

    private final MigrationRepository repository;
    private final SensorService sensorService;
    private final CosmosMigrationService cosmosMigrationService;

    @Autowired
    public MigrationController(MigrationRepository repository,
                               SensorService sensorService,
                               CosmosMigrationService cosmosMigrationService) {
        this.repository = repository;
        this.sensorService = sensorService;
        this.cosmosMigrationService = cosmosMigrationService;
    }

    @PostMapping("/insert")
    public MigrationRecord insertRecord(@RequestBody MigrationRecord record) {
        return repository.save(record);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MigrationRecord>> getAllRecords() {
        Iterable<MigrationRecord> recordsIterable = repository.findAll();
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
        return ResponseEntity.ok(repository.findAfterTimeStamp(timestamp));
    }
    @GetMapping("/runMigration")
    public ResponseEntity<String> runMigration() {
        cosmosMigrationService.runMigration();
        return ResponseEntity.ok("Success");
    }
}