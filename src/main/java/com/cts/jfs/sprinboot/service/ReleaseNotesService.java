package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.ReleaseNote;
import com.cts.jfs.sprinboot.model.CustomerAdvisory;
import com.cts.jfs.sprinboot.repository.ReleaseNoteRepository;
import com.cts.jfs.sprinboot.repository.CustomerAdvisoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import com.cts.jfs.sprinboot.exception.ResourceNotFoundException;
import com.cts.jfs.sprinboot.exception.DuplicateRecordException;
import com.cts.jfs.sprinboot.exception.InvalidOperationException;
import com.cts.jfs.sprinboot.exception.UnauthorizedException;
@Service
public class ReleaseNotesService {

    @Autowired
    private ReleaseNoteRepository releaseNoteRepository;

    @Autowired
    private CustomerAdvisoryRepository customerAdvisoryRepository;

    // ===== RELEASE NOTES =====

    // Get all release notes
    public List<ReleaseNote> getAllReleaseNotes() {
        return releaseNoteRepository.findAll();
    }

    // Get release note by ID
    public ReleaseNote getReleaseNoteById(Long id) {
        return releaseNoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReleaseNote not found with ID: " + id));
    }

    // Save release note
    public ReleaseNote saveReleaseNote(ReleaseNote note) {
        if (note.getStatus() == null) {
            note.setStatus(ReleaseNote.NoteStatus.Draft);
        }
        return releaseNoteRepository.save(note);
    }

    // Update release note
    public ReleaseNote updateReleaseNote(Long id, ReleaseNote updated) {
        ReleaseNote existing = getReleaseNoteById(id);
        existing.setVersionNumber(updated.getVersionNumber());
        existing.setSummary(updated.getSummary());
        existing.setNewFeatures(updated.getNewFeatures());
        existing.setBugFixes(updated.getBugFixes());
        existing.setKnownIssues(updated.getKnownIssues());
        existing.setDeprecations(updated.getDeprecations());
        existing.setUpgradeInstructions(updated.getUpgradeInstructions());
        existing.setStatus(updated.getStatus());
        return releaseNoteRepository.save(existing);
    }

    // Delete release note
    public void deleteReleaseNote(Long id) {
        releaseNoteRepository.deleteById(id);
    }

    // Get notes by release
    public List<ReleaseNote> getNotesByRelease(Long releaseId) {
        return releaseNoteRepository.findByReleaseID(releaseId);
    }

    // Get notes by status
    public List<ReleaseNote> getNotesByStatus(ReleaseNote.NoteStatus status) {
        return releaseNoteRepository.findByStatus(status);
    }

    // Publish release note
    public ReleaseNote publishReleaseNote(Long id) {
        ReleaseNote note = getReleaseNoteById(id);
        if (note.getStatus() != ReleaseNote.NoteStatus.Approved) {
            throw new RuntimeException("Only Approved notes can be published. Current status: " + note.getStatus());
        }
        note.setStatus(ReleaseNote.NoteStatus.Published);
        return releaseNoteRepository.save(note);
    }

    // Approve release note
    public ReleaseNote approveReleaseNote(Long id) {
        ReleaseNote note = getReleaseNoteById(id);
        note.setStatus(ReleaseNote.NoteStatus.Approved);
        return releaseNoteRepository.save(note);
    }

    // ===== CUSTOMER ADVISORIES =====

    // Get all advisories
    public List<CustomerAdvisory> getAllAdvisories() {
        return customerAdvisoryRepository.findAll();
    }

    // Get advisory by ID
    public CustomerAdvisory getAdvisoryById(Long id) {
        return customerAdvisoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CustomerAdvisory not found with ID: " + id));
    }

    // Save advisory
    public CustomerAdvisory saveAdvisory(CustomerAdvisory advisory) {
        if (advisory.getStatus() == null) {
            advisory.setStatus(CustomerAdvisory.AdvisoryStatus.Draft);
        }
        return customerAdvisoryRepository.save(advisory);
    }

    // Update advisory
    public CustomerAdvisory updateAdvisory(Long id, CustomerAdvisory updated) {
        CustomerAdvisory existing = getAdvisoryById(id);
        existing.setTitle(updated.getTitle());
        existing.setSeverity(updated.getSeverity());
        existing.setAffectedVersions(updated.getAffectedVersions());
        existing.setResolution(updated.getResolution());
        existing.setStatus(updated.getStatus());
        return customerAdvisoryRepository.save(existing);
    }

    // Delete advisory
    public void deleteAdvisory(Long id) {
        customerAdvisoryRepository.deleteById(id);
    }

    // Get advisories by release
    public List<CustomerAdvisory> getAdvisoriesByRelease(Long releaseId) {
        return customerAdvisoryRepository.findByReleaseID(releaseId);
    }

    // Get advisories by severity
    public List<CustomerAdvisory> getAdvisoriesBySeverity(CustomerAdvisory.Severity severity) {
        return customerAdvisoryRepository.findBySeverity(severity);
    }

    // Publish advisory
    public CustomerAdvisory publishAdvisory(Long id) {
        CustomerAdvisory advisory = getAdvisoryById(id);
        advisory.setStatus(CustomerAdvisory.AdvisoryStatus.Published);
        advisory.setPublishedDate(LocalDate.now());
        return customerAdvisoryRepository.save(advisory);
    }

    // Archive advisory
    public CustomerAdvisory archiveAdvisory(Long id) {
        CustomerAdvisory advisory = getAdvisoryById(id);
        advisory.setStatus(CustomerAdvisory.AdvisoryStatus.Archived);
        return customerAdvisoryRepository.save(advisory);
    }
}
