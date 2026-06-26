package com.cts.jfs.sprinboot.controller;

import com.cts.jfs.sprinboot.model.ReleasePackage;
import com.cts.jfs.sprinboot.service.ReleasePackageService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/releases")
@CrossOrigin(origins = "http://localhost:3000")
public class ReleasePackageController {

    @Autowired
    private ReleasePackageService releasePackageService;

    // View - all roles
    @GetMapping("/all")
    public ResponseEntity<List<ReleasePackage>> getAllReleases() {
        return ResponseEntity.ok(releasePackageService.getAllReleases());
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<ReleasePackage> getReleaseById(@PathVariable Long id) {
        return ResponseEntity.ok(releasePackageService.getReleaseById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReleasePackage>> getReleasesByProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(releasePackageService.getReleasesByProduct(productId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReleasePackage>> getReleasesByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(releasePackageService.getReleasesByStatus(
                ReleasePackage.ReleaseStatus.valueOf(status)));
    }

    // Create - Admin, ReleaseManager, DevLead
    @PostMapping("/save")
    public ResponseEntity<ReleasePackage> saveRelease(
            @RequestBody ReleasePackage release) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(releasePackageService.saveRelease(release));
    }

    // Update - Admin, ReleaseManager only
    @PostMapping("/update/{id}")
    public ResponseEntity<ReleasePackage> updateRelease(
            @PathVariable Long id,
            @RequestBody ReleasePackage release) {
        return ResponseEntity.ok(releasePackageService.updateRelease(id, release));
    }

    // Delete - Admin, ReleaseManager only
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRelease(@PathVariable Long id) {
        releasePackageService.deleteRelease(id);
        return ResponseEntity.ok("Release deleted successfully");
    }

    // Recall - Admin, ReleaseManager only
    @PostMapping("/recall/{id}")
    public ResponseEntity<ReleasePackage> recallRelease(@PathVariable Long id) {
        return ResponseEntity.ok(releasePackageService.recallRelease(id));
    }
}