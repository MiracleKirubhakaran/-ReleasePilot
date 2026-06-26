package com.cts.jfs.sprinboot.controller;

import com.cts.jfs.sprinboot.model.Environment;
import com.cts.jfs.sprinboot.model.PromotionRequest;
import com.cts.jfs.sprinboot.model.ChangeFreezeWindow;
import com.cts.jfs.sprinboot.service.EnvironmentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/environments")
@CrossOrigin(origins = "http://localhost:3000")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    // ===== ENVIRONMENTS =====

    @GetMapping("/all")
    public ResponseEntity<List<Environment>> getAllEnvironments() {
        return ResponseEntity.ok(environmentService.getAllEnvironments());
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Environment> getEnvironmentById(@PathVariable Long id) {
        return ResponseEntity.ok(environmentService.getEnvironmentById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Environment>> getEnvironmentsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(environmentService.getEnvironmentsByProduct(productId));
    }

    @PostMapping("/save")
    public ResponseEntity<Environment> saveEnvironment(@RequestBody Environment environment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(environmentService.saveEnvironment(environment));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Environment> updateEnvironment(@PathVariable Long id,
                                                          @RequestBody Environment environment) {
        return ResponseEntity.ok(environmentService.updateEnvironment(id, environment));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEnvironment(@PathVariable Long id) {
        environmentService.deleteEnvironment(id);
        return ResponseEntity.ok("Environment deleted successfully");
    }

    // ===== PROMOTION REQUESTS =====

    @GetMapping("/promotions/all")
    public ResponseEntity<List<PromotionRequest>> getAllPromotions() {
        return ResponseEntity.ok(environmentService.getAllPromotions());
    }

    @PostMapping("/promotions/save")
    public ResponseEntity<PromotionRequest> savePromotion(@RequestBody PromotionRequest promotion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(environmentService.savePromotion(promotion));
    }

    @PostMapping("/promotions/approve/{id}")
    public ResponseEntity<PromotionRequest> approvePromotion(@PathVariable Long id,
                                                              @RequestParam Long approvedByID) {
        return ResponseEntity.ok(environmentService.approvePromotion(id, approvedByID));
    }

    @PostMapping("/promotions/reject/{id}")
    public ResponseEntity<PromotionRequest> rejectPromotion(@PathVariable Long id) {
        return ResponseEntity.ok(environmentService.rejectPromotion(id));
    }

    @PostMapping("/promotions/rollback/{id}")
    public ResponseEntity<PromotionRequest> rollbackPromotion(@PathVariable Long id) {
        return ResponseEntity.ok(environmentService.rollbackPromotion(id));
    }

    // ===== CHANGE FREEZE WINDOWS =====

    @GetMapping("/freeze/all")
    public ResponseEntity<List<ChangeFreezeWindow>> getAllFreezeWindows() {
        return ResponseEntity.ok(environmentService.getAllFreezeWindows());
    }

    @PostMapping("/freeze/save")
    public ResponseEntity<ChangeFreezeWindow> saveFreezeWindow(
            @RequestBody ChangeFreezeWindow freeze) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(environmentService.saveFreezeWindow(freeze));
    }

    @PostMapping("/freeze/lift/{id}")
    public ResponseEntity<ChangeFreezeWindow> liftFreezeWindow(@PathVariable Long id) {
        return ResponseEntity.ok(environmentService.liftFreezeWindow(id));
    }

    @GetMapping("/freeze/frozen/{productId}")
    public ResponseEntity<Boolean> isProductFrozen(@PathVariable Long productId) {
        return ResponseEntity.ok(environmentService.isProductFrozen(productId));
    }
}
