package org.example.azurecosmomigration.controller;

import org.example.azurecosmomigration.model.MigrationRecord;
import org.example.azurecosmomigration.repo.MigrationRepository;
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

    @Autowired
    public MigrationController(MigrationRepository repository) {
        this.repository = repository;
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
}