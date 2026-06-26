package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.Environment;
import com.cts.jfs.sprinboot.model.PromotionRequest;
import com.cts.jfs.sprinboot.model.ChangeFreezeWindow;
import com.cts.jfs.sprinboot.repository.EnvironmentRepository;
import com.cts.jfs.sprinboot.repository.PromotionRequestRepository;
import com.cts.jfs.sprinboot.repository.ChangeFreezeWindowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class EnvironmentService {

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private PromotionRequestRepository promotionRequestRepository;

    @Autowired
    private ChangeFreezeWindowRepository changeFreezeWindowRepository;

    // ===== ENVIRONMENTS =====

    // Get all environments
    public List<Environment> getAllEnvironments() {
        return environmentRepository.findAll();
    }

    // Get environment by ID
    public Environment getEnvironmentById(Long id) {
        return environmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Environment not found with ID: " + id));
    }

    // Save environment
    public Environment saveEnvironment(Environment environment) {
        if (environment.getStatus() == null) {
            environment.setStatus(Environment.EnvStatus.Active);
        }
        return environmentRepository.save(environment);
    }

    // Update environment
    public Environment updateEnvironment(Long id, Environment updated) {
        Environment existing = getEnvironmentById(id);
        existing.setEnvName(updated.getEnvName());
        existing.setOwnerID(updated.getOwnerID());
        existing.setCurrentVersion(updated.getCurrentVersion());
        existing.setLastPromotionDate(updated.getLastPromotionDate());
        existing.setStatus(updated.getStatus());
        return environmentRepository.save(existing);
    }

    // Delete environment
    public void deleteEnvironment(Long id) {
        environmentRepository.deleteById(id);
    }

    // Get environments by product
    public List<Environment> getEnvironmentsByProduct(Long productId) {
        return environmentRepository.findByProductID(productId);
    }

    // Get environments by status
    public List<Environment> getEnvironmentsByStatus(Environment.EnvStatus status) {
        return environmentRepository.findByStatus(status);
    }

    // ===== PROMOTION REQUESTS =====

    // Get all promotions
    public List<PromotionRequest> getAllPromotions() {
        return promotionRequestRepository.findAll();
    }

    // Get promotion by ID
    public PromotionRequest getPromotionById(Long id) {
        return promotionRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PromotionRequest not found with ID: " + id));
    }

    // Save promotion request
    public PromotionRequest savePromotion(PromotionRequest promotion) {
        promotion.setStatus(PromotionRequest.PromotionStatus.Pending);
        return promotionRequestRepository.save(promotion);
    }

    // Approve promotion
    public PromotionRequest approvePromotion(Long id, Long approvedById) {
        PromotionRequest promotion = getPromotionById(id);
        promotion.setApprovedByID(approvedById);
        promotion.setStatus(PromotionRequest.PromotionStatus.Approved);
        return promotionRequestRepository.save(promotion);
    }

    // Reject promotion
    public PromotionRequest rejectPromotion(Long id) {
        PromotionRequest promotion = getPromotionById(id);
        promotion.setStatus(PromotionRequest.PromotionStatus.Rejected);
        return promotionRequestRepository.save(promotion);
    }

    // Rollback promotion
    public PromotionRequest rollbackPromotion(Long id) {
        PromotionRequest promotion = getPromotionById(id);
        promotion.setStatus(PromotionRequest.PromotionStatus.RolledBack);
        return promotionRequestRepository.save(promotion);
    }

    // Get promotions by release
    public List<PromotionRequest> getPromotionsByRelease(Long releaseId) {
        return promotionRequestRepository.findByReleaseID(releaseId);
    }

    // Get promotions by status
    public List<PromotionRequest> getPromotionsByStatus(PromotionRequest.PromotionStatus status) {
        return promotionRequestRepository.findByStatus(status);
    }

    // ===== CHANGE FREEZE WINDOWS =====

    // Get all freeze windows
    public List<ChangeFreezeWindow> getAllFreezeWindows() {
        return changeFreezeWindowRepository.findAll();
    }

    // Get freeze window by ID
    public ChangeFreezeWindow getFreezeWindowById(Long id) {
        return changeFreezeWindowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ChangeFreezeWindow not found with ID: " + id));
    }

    // Save freeze window
    public ChangeFreezeWindow saveFreezeWindow(ChangeFreezeWindow freeze) {
        if (freeze.getStatus() == null) {
            freeze.setStatus(ChangeFreezeWindow.FreezeStatus.Scheduled);
        }
        return changeFreezeWindowRepository.save(freeze);
    }

    // Lift freeze window
    public ChangeFreezeWindow liftFreezeWindow(Long id) {
        ChangeFreezeWindow freeze = getFreezeWindowById(id);
        freeze.setStatus(ChangeFreezeWindow.FreezeStatus.Lifted);
        return changeFreezeWindowRepository.save(freeze);
    }

    // Get freeze windows by product
    public List<ChangeFreezeWindow> getFreezeWindowsByProduct(Long productId) {
        return changeFreezeWindowRepository.findByProductID(productId);
    }

    // Check if product is currently frozen
    public boolean isProductFrozen(Long productId) {
        LocalDate today = LocalDate.now();
        List<ChangeFreezeWindow> activeFreeze = changeFreezeWindowRepository
                .findByProductIDAndStatus(productId, ChangeFreezeWindow.FreezeStatus.Active);
        return activeFreeze.stream()
                .anyMatch(f -> !today.isBefore(f.getStartDate()) && !today.isAfter(f.getEndDate()));
    }
}
