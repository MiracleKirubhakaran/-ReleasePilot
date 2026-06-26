package com.cts.jfs.sprinboot.controller;

import com.cts.jfs.sprinboot.model.TestSuite;
import com.cts.jfs.sprinboot.model.TestExecution;
import com.cts.jfs.sprinboot.model.ReleaseGate;
import com.cts.jfs.sprinboot.service.QAService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/qa")
@CrossOrigin(origins = "http://localhost:3000")
public class QAController {

    @Autowired
    private QAService qaService;

    // ===== TEST SUITES =====

    @GetMapping("/suites/all")
    public ResponseEntity<List<TestSuite>> getAllTestSuites() {
        return ResponseEntity.ok(qaService.getAllTestSuites());
    }

    @GetMapping("/suites/view/{id}")
    public ResponseEntity<TestSuite> getTestSuiteById(@PathVariable Long id) {
        return ResponseEntity.ok(qaService.getTestSuiteById(id));
    }

    @PostMapping("/suites/save")
    public ResponseEntity<TestSuite> saveTestSuite(@RequestBody TestSuite suite) {
        return ResponseEntity.status(HttpStatus.CREATED).body(qaService.saveTestSuite(suite));
    }

    @PostMapping("/suites/update/{id}")
    public ResponseEntity<TestSuite> updateTestSuite(@PathVariable Long id,
                                                      @RequestBody TestSuite suite) {
        return ResponseEntity.ok(qaService.updateTestSuite(id, suite));
    }

    @DeleteMapping("/suites/delete/{id}")
    public ResponseEntity<String> deleteTestSuite(@PathVariable Long id) {
        qaService.deleteTestSuite(id);
        return ResponseEntity.ok("Test suite deleted successfully");
    }

    // ===== TEST EXECUTIONS =====

    @GetMapping("/executions/all")
    public ResponseEntity<List<TestExecution>> getAllTestExecutions() {
        return ResponseEntity.ok(qaService.getAllTestExecutions());
    }

    @GetMapping("/executions/release/{releaseId}")
    public ResponseEntity<List<TestExecution>> getExecutionsByRelease(@PathVariable Long releaseId) {
        return ResponseEntity.ok(qaService.getExecutionsByRelease(releaseId));
    }

    @PostMapping("/executions/save")
    public ResponseEntity<TestExecution> saveTestExecution(@RequestBody TestExecution execution) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(qaService.saveTestExecution(execution));
    }

    @PostMapping("/executions/update/{id}")
    public ResponseEntity<TestExecution> updateTestExecution(@PathVariable Long id,
                                                              @RequestBody TestExecution execution) {
        return ResponseEntity.ok(qaService.updateTestExecution(id, execution));
    }

    // ===== RELEASE GATES =====

    @GetMapping("/gates/all")
    public ResponseEntity<List<ReleaseGate>> getAllReleaseGates() {
        return ResponseEntity.ok(qaService.getAllReleaseGates());
    }

    @GetMapping("/gates/release/{releaseId}")
    public ResponseEntity<List<ReleaseGate>> getGatesByRelease(@PathVariable Long releaseId) {
        return ResponseEntity.ok(qaService.getGatesByRelease(releaseId));
    }

    @PostMapping("/gates/save")
    public ResponseEntity<ReleaseGate> saveReleaseGate(@RequestBody ReleaseGate gate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(qaService.saveReleaseGate(gate));
    }

    @PostMapping("/gates/evaluate/{id}")
    public ResponseEntity<ReleaseGate> evaluateGate(@PathVariable Long id,
                                                     @RequestParam String outcome,
                                                     @RequestParam Long evaluatedByID) {
        return ResponseEntity.ok(qaService.evaluateGate(
                id, ReleaseGate.GateOutcome.valueOf(outcome), evaluatedByID));
    }

    @DeleteMapping("/gates/delete/{id}")
    public ResponseEntity<String> deleteGate(@PathVariable Long id) {
        qaService.deleteReleaseGate(id);
        return ResponseEntity.ok("Release gate deleted successfully");
    }

    @GetMapping("/gates/allpassed/{releaseId}")
    public ResponseEntity<Boolean> allGatesPassed(@PathVariable Long releaseId) {
        return ResponseEntity.ok(qaService.allGatesPassed(releaseId));
    }
    
    @GetMapping("/gates/view/{id}")
    public ResponseEntity<ReleaseGate> getReleaseGateById(@PathVariable Long id) {
        return ResponseEntity.ok(qaService.getReleaseGateById(id));
    }
}
