package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.TestSuite;
import com.cts.jfs.sprinboot.model.TestExecution;
import com.cts.jfs.sprinboot.model.ReleaseGate;
import com.cts.jfs.sprinboot.repository.TestSuiteRepository;
import com.cts.jfs.sprinboot.repository.TestExecutionRepository;
import com.cts.jfs.sprinboot.repository.ReleaseGateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.cts.jfs.sprinboot.exception.ResourceNotFoundException;
import com.cts.jfs.sprinboot.exception.DuplicateRecordException;
import com.cts.jfs.sprinboot.exception.InvalidOperationException;
import com.cts.jfs.sprinboot.exception.UnauthorizedException;
@Service
public class QAService {

    @Autowired
    private TestSuiteRepository testSuiteRepository;

    @Autowired
    private TestExecutionRepository testExecutionRepository;

    @Autowired
    private ReleaseGateRepository releaseGateRepository;

    // ===== TEST SUITES =====

    // Get all test suites
    public List<TestSuite> getAllTestSuites() {
        return testSuiteRepository.findAll();
    }

    // Get test suite by ID
    public TestSuite getTestSuiteById(Long id) {
        return testSuiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestSuite not found with ID: " + id));
    }

    // Save test suite
    public TestSuite saveTestSuite(TestSuite suite) {
        if (suite.getStatus() == null) {
            suite.setStatus(TestSuite.SuiteStatus.Active);
        }
        return testSuiteRepository.save(suite);
    }

    // Update test suite
    public TestSuite updateTestSuite(Long id, TestSuite updated) {
        TestSuite existing = getTestSuiteById(id);
        existing.setSuiteName(updated.getSuiteName());
        existing.setType(updated.getType());
        existing.setTotalTestCases(updated.getTotalTestCases());
        existing.setStatus(updated.getStatus());
        return testSuiteRepository.save(existing);
    }

    // Delete test suite
    public void deleteTestSuite(Long id) {
        testSuiteRepository.deleteById(id);
    }

    // Get test suites by product
    public List<TestSuite> getTestSuitesByProduct(Long productId) {
        return testSuiteRepository.findByProductID(productId);
    }

    // Get test suites by type
    public List<TestSuite> getTestSuitesByType(TestSuite.SuiteType type) {
        return testSuiteRepository.findByType(type);
    }

    // ===== TEST EXECUTIONS =====

    // Get all test executions
    public List<TestExecution> getAllTestExecutions() {
        return testExecutionRepository.findAll();
    }

    // Get test execution by ID
    public TestExecution getExecutionById(Long id) {
        return testExecutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestExecution not found with ID: " + id));
    }

    // Save test execution
    public TestExecution saveTestExecution(TestExecution execution) {
        if (execution.getStatus() == null) {
            execution.setStatus(TestExecution.ExecutionStatus.InProgress);
        }
        // Auto-calculate coverage percent
        if (execution.getTotalRun() != null && execution.getTotalRun() > 0
                && execution.getPassed() != null) {
            double coverage = ((double) execution.getPassed() / execution.getTotalRun()) * 100;
            execution.setCoveragePercent(Math.round(coverage * 100.0) / 100.0);
        }
        return testExecutionRepository.save(execution);
    }

    // Update test execution
    public TestExecution updateTestExecution(Long id, TestExecution updated) {
        TestExecution existing = getExecutionById(id);
        existing.setTotalRun(updated.getTotalRun());
        existing.setPassed(updated.getPassed());
        existing.setFailed(updated.getFailed());
        existing.setSkipped(updated.getSkipped());
        existing.setEndDate(updated.getEndDate());
        existing.setStatus(updated.getStatus());
        // Recalculate coverage
        if (updated.getTotalRun() != null && updated.getTotalRun() > 0
                && updated.getPassed() != null) {
            double coverage = ((double) updated.getPassed() / updated.getTotalRun()) * 100;
            existing.setCoveragePercent(Math.round(coverage * 100.0) / 100.0);
        }
        return testExecutionRepository.save(existing);
    }

    // Get executions by release
    public List<TestExecution> getExecutionsByRelease(Long releaseId) {
        return testExecutionRepository.findByReleaseID(releaseId);
    }

    // Get executions by suite
    public List<TestExecution> getExecutionsBySuite(Long suiteId) {
        return testExecutionRepository.findBySuiteID(suiteId);
    }

    // ===== RELEASE GATES =====

    // Get all release gates
    public List<ReleaseGate> getAllReleaseGates() {
        return releaseGateRepository.findAll();
    }

    // Get release gate by ID
    public ReleaseGate getReleaseGateById(Long id) {
        return releaseGateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReleaseGate not found with ID: " + id));
    }

    // Save release gate
    public ReleaseGate saveReleaseGate(ReleaseGate gate) {
        if (gate.getStatus() == null) {
            gate.setStatus(ReleaseGate.GateStatus.Pending);
        }
        return releaseGateRepository.save(gate);
    }

    // Evaluate gate outcome
    public ReleaseGate evaluateGate(Long id, ReleaseGate.GateOutcome outcome, Long evaluatedById) {
        ReleaseGate gate = getReleaseGateById(id);
        gate.setOutcome(outcome);
        gate.setEvaluatedByID(evaluatedById);
        gate.setStatus(ReleaseGate.GateStatus.Evaluated);
        return releaseGateRepository.save(gate);
    }

    // Get gates by release
    public List<ReleaseGate> getGatesByRelease(Long releaseId) {
        return releaseGateRepository.findByReleaseID(releaseId);
    }

    // Delete release gate
    public void deleteReleaseGate(Long id) {
        releaseGateRepository.deleteById(id);
    }

    // Check if all gates passed for a release
    public boolean allGatesPassed(Long releaseId) {
        List<ReleaseGate> gates = getGatesByRelease(releaseId);
        return gates.stream()
                .allMatch(g -> g.getOutcome() == ReleaseGate.GateOutcome.Pass
                        || g.getOutcome() == ReleaseGate.GateOutcome.Override);
    }
}
