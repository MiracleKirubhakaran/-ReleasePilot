package com.cts.jfs.sprinboot.controller;

import com.cts.jfs.sprinboot.model.ReleaseNote;
import com.cts.jfs.sprinboot.model.CustomerAdvisory;
import com.cts.jfs.sprinboot.service.ReleaseNotesService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/releasenotes")
@CrossOrigin(origins = "http://localhost:3000")
public class ReleaseNotesController {

    @Autowired
    private ReleaseNotesService releaseNotesService;

    // ===== RELEASE NOTES =====

    @GetMapping("/all")
    public ResponseEntity<List<ReleaseNote>> getAllReleaseNotes() {
        return ResponseEntity.ok(releaseNotesService.getAllReleaseNotes());
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<ReleaseNote> getReleaseNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(releaseNotesService.getReleaseNoteById(id));
    }

    @GetMapping("/release/{releaseId}")
    public ResponseEntity<List<ReleaseNote>> getNotesByRelease(@PathVariable Long releaseId) {
        return ResponseEntity.ok(releaseNotesService.getNotesByRelease(releaseId));
    }

    @PostMapping("/save")
    public ResponseEntity<ReleaseNote> saveReleaseNote(@RequestBody ReleaseNote note) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(releaseNotesService.saveReleaseNote(note));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ReleaseNote> updateReleaseNote(@PathVariable Long id,
                                                          @RequestBody ReleaseNote note) {
        return ResponseEntity.ok(releaseNotesService.updateReleaseNote(id, note));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReleaseNote(@PathVariable Long id) {
        releaseNotesService.deleteReleaseNote(id);
        return ResponseEntity.ok("Release note deleted successfully");
    }

    @PostMapping("/publish/{id}")
    public ResponseEntity<ReleaseNote> publishReleaseNote(@PathVariable Long id) {
        return ResponseEntity.ok(releaseNotesService.publishReleaseNote(id));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<ReleaseNote> approveReleaseNote(@PathVariable Long id) {
        return ResponseEntity.ok(releaseNotesService.approveReleaseNote(id));
    }

    // ===== CUSTOMER ADVISORIES =====

    @GetMapping("/advisories/all")
    public ResponseEntity<List<CustomerAdvisory>> getAllAdvisories() {
        return ResponseEntity.ok(releaseNotesService.getAllAdvisories());
    }

    @GetMapping("/advisories/view/{id}")
    public ResponseEntity<CustomerAdvisory> getAdvisoryById(@PathVariable Long id) {
        return ResponseEntity.ok(releaseNotesService.getAdvisoryById(id));
    }

    @PostMapping("/advisories/save")
    public ResponseEntity<CustomerAdvisory> saveAdvisory(@RequestBody CustomerAdvisory advisory) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(releaseNotesService.saveAdvisory(advisory));
    }

    @PostMapping("/advisories/update/{id}")
    public ResponseEntity<CustomerAdvisory> updateAdvisory(@PathVariable Long id,
                                                            @RequestBody CustomerAdvisory advisory) {
        return ResponseEntity.ok(releaseNotesService.updateAdvisory(id, advisory));
    }

    @DeleteMapping("/advisories/delete/{id}")
    public ResponseEntity<String> deleteAdvisory(@PathVariable Long id) {
        releaseNotesService.deleteAdvisory(id);
        return ResponseEntity.ok("Advisory deleted successfully");
    }

    @PostMapping("/advisories/publish/{id}")
    public ResponseEntity<CustomerAdvisory> publishAdvisory(@PathVariable Long id) {
        return ResponseEntity.ok(releaseNotesService.publishAdvisory(id));
    }
}
