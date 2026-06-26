package com.cts.jfs.sprinboot.controller;

import com.cts.jfs.sprinboot.model.RoadmapMilestone;
import com.cts.jfs.sprinboot.service.RoadmapMilestoneService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/milestones")
@CrossOrigin(origins = "http://localhost:3000")
public class RoadmapMilestoneController {

    @Autowired
    private RoadmapMilestoneService milestoneService;

    // Get all milestones
    @GetMapping("/all")
    public ResponseEntity<List<RoadmapMilestone>> getAllMilestones() {
        return ResponseEntity.ok(milestoneService.getAllMilestones());
    }

    // Get milestone by ID
    @GetMapping("/view/{id}")
    public ResponseEntity<RoadmapMilestone> getMilestoneById(@PathVariable Long id) {
        return ResponseEntity.ok(milestoneService.getMilestoneById(id));
    }

    // Get milestones by product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<RoadmapMilestone>> getMilestonesByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(milestoneService.getMilestonesByProduct(productId));
    }

    // Save new milestone
    @PostMapping("/save")
    public ResponseEntity<RoadmapMilestone> saveMilestone(@RequestBody RoadmapMilestone milestone) {
        return ResponseEntity.status(HttpStatus.CREATED).body(milestoneService.saveMilestone(milestone));
    }

    // Update milestone
    @PostMapping("/update/{id}")
    public ResponseEntity<RoadmapMilestone> updateMilestone(@PathVariable Long id,
                                                             @RequestBody RoadmapMilestone milestone) {
        return ResponseEntity.ok(milestoneService.updateMilestone(id, milestone));
    }

    // Delete milestone
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMilestone(@PathVariable Long id) {
        milestoneService.deleteMilestone(id);
        return ResponseEntity.ok("Milestone deleted successfully");
    }

    // Get by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RoadmapMilestone>> getMilestonesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(milestoneService.getMilestonesByStatus(
                RoadmapMilestone.MilestoneStatus.valueOf(status)));
    }
}
