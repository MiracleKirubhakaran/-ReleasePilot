package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.ReleasePackage;
import com.cts.jfs.sprinboot.model.PatchDependency;
import com.cts.jfs.sprinboot.repository.ReleasePackageRepository;
import com.cts.jfs.sprinboot.repository.PatchDependencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReleasePackageService {

    @Autowired
    private ReleasePackageRepository releasePackageRepository;

    @Autowired
    private PatchDependencyRepository patchDependencyRepository;

    // Get all releases
    public List<ReleasePackage> getAllReleases() {
        return releasePackageRepository.findAll();
    }

    // Get release by ID
    public ReleasePackage getReleaseById(Long id) {
        return releasePackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReleasePackage not found with ID: " + id));
    }

    // Save new release
    public ReleasePackage saveRelease(ReleasePackage release) {
        if (release.getStatus() == null) {
            release.setStatus(ReleasePackage.ReleaseStatus.Draft);
        }
        if (release.getStatus() == ReleasePackage.ReleaseStatus.Released ||
                release.getStatus() == ReleasePackage.ReleaseStatus.Recalled) {
                release.setStatus(ReleasePackage.ReleaseStatus.Draft);
            }
        
        return releasePackageRepository.save(release);
    }

    // Update release
    public ReleasePackage updateRelease(Long id, ReleasePackage updated) {
        ReleasePackage existing = getReleaseById(id);
        existing.setVersionNumber(updated.getVersionNumber());
        existing.setReleaseType(updated.getReleaseType());
        existing.setIncludedItemIDs(updated.getIncludedItemIDs());
        existing.setReleaseNotesDraft(updated.getReleaseNotesDraft());
        existing.setTargetReleaseDate(updated.getTargetReleaseDate());
        existing.setReleasedByID(updated.getReleasedByID());
        existing.setStatus(updated.getStatus());
        return releasePackageRepository.save(existing);
    }

    // Delete release
    public void deleteRelease(Long id) {
        releasePackageRepository.deleteById(id);
    }

    // Get releases by product
    public List<ReleasePackage> getReleasesByProduct(Long productId) {
        return releasePackageRepository.findByProductID(productId);
    }

    // Get releases by status
    public List<ReleasePackage> getReleasesByStatus(ReleasePackage.ReleaseStatus status) {
        return releasePackageRepository.findByStatus(status);
    }

    // Get releases by type
    public List<ReleasePackage> getReleasesByType(ReleasePackage.ReleaseType type) {
        return releasePackageRepository.findByReleaseType(type);
    }

    // Promote release status
    public ReleasePackage promoteReleaseStatus(Long id, ReleasePackage.ReleaseStatus newStatus) {
        ReleasePackage release = getReleaseById(id);
        release.setStatus(newStatus);
        return releasePackageRepository.save(release);
    }

    // Recall a release
    public ReleasePackage recallRelease(Long id) {
        return promoteReleaseStatus(id, ReleasePackage.ReleaseStatus.Recalled);
    }

    // ===== PATCH DEPENDENCIES =====

    // Save patch dependency
    public PatchDependency savePatchDependency(PatchDependency dependency) {
        return patchDependencyRepository.save(dependency);
    }

    // Get dependencies by release
    public List<PatchDependency> getDependenciesByRelease(Long releaseId) {
        return patchDependencyRepository.findByReleaseID(releaseId);
    }

    // Delete patch dependency
    public void deletePatchDependency(Long id) {
        patchDependencyRepository.deleteById(id);
    }
}
