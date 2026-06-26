package com.cts.jfs.sprinboot.controller;

import com.cts.jfs.sprinboot.model.Sprint;
import com.cts.jfs.sprinboot.model.SprintItem;
import com.cts.jfs.sprinboot.service.SprintService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/sprints")
@CrossOrigin(origins = "http://localhost:3000")
public class SprintController {

    @Autowired
    private SprintService sprintService;

    // Get all sprints
    @GetMapping("/all")
    public ResponseEntity<List<Sprint>> getAllSprints() {
        return ResponseEntity.ok(sprintService.getAllSprints());
    }

    // Get sprint by ID with sprint items
    @GetMapping("/view/{id}")
    public ResponseEntity<Map<String, Object>> getSprintById(@PathVariable Long id) {
        Sprint sprint = sprintService.getSprintById(id);
        List<SprintItem> sprintItems = sprintService.getSprintItemsBySprintId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("sprint", sprint);
        response.put("sprintItems", sprintItems);
        return ResponseEntity.ok(response);
    }

    // Get sprints by product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Sprint>> getSprintsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(sprintService.getSprintsByProduct(productId));
    }

    // Save sprint
    @PostMapping("/save")
    public ResponseEntity<Sprint> saveSprint(@RequestBody Sprint sprint) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sprintService.saveSprint(sprint));
    }

    // Update sprint
    @PostMapping("/update/{id}")
    public ResponseEntity<Sprint> updateSprint(@PathVariable Long id, @RequestBody Sprint sprint) {
        return ResponseEntity.ok(sprintService.updateSprint(id, sprint));
    }

    // Delete sprint
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSprint(@PathVariable Long id) {
        sprintService.deleteSprint(id);
        return ResponseEntity.ok("Sprint deleted successfully");
    }

    // Add item to sprint
    @PostMapping("/addItem")
    public ResponseEntity<SprintItem> addItemToSprint(@RequestBody SprintItem sprintItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sprintService.addItemToSprint(sprintItem));
    }

    // Update sprint item status
    @PostMapping("/updateItemStatus/{sprintItemId}")
    public ResponseEntity<SprintItem> updateSprintItemStatus(
            @PathVariable Long sprintItemId,
            @RequestParam String status) {
        return ResponseEntity.ok(sprintService.updateSprintItemStatus(
                sprintItemId, SprintItem.SprintItemStatus.valueOf(status)));
    }

    // Remove sprint item
    @DeleteMapping("/removeItem/{sprintItemId}")
    public ResponseEntity<String> removeSprintItem(@PathVariable Long sprintItemId) {
        sprintService.removeSprintItem(sprintItemId);
        return ResponseEntity.ok("Sprint item removed successfully");
    }
}
