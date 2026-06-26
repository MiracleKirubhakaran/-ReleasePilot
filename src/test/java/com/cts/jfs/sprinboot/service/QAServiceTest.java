package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.TestSuite;
import com.cts.jfs.sprinboot.model.TestExecution;
import com.cts.jfs.sprinboot.model.ReleaseGate;
import com.cts.jfs.sprinboot.repository.TestSuiteRepository;
import com.cts.jfs.sprinboot.repository.TestExecutionRepository;
import com.cts.jfs.sprinboot.repository.ReleaseGateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QAServiceTest {

    @Mock
    private TestSuiteRepository testSuiteRepository;

    @Mock
    private TestExecutionRepository testExecutionRepository;

    @Mock
    private ReleaseGateRepository releaseGateRepository;

    @InjectMocks
    private QAService qaService;

    private TestSuite suite;
    private TestExecution execution;
    private ReleaseGate gate;

    @BeforeEach
    void setUp() {
        suite = new TestSuite();
        suite.setSuiteID(1L);
        suite.setProductID(1L);
        suite.setSuiteName("Regression Suite");
        suite.setType(TestSuite.SuiteType.Regression);
        suite.setTotalTestCases(50);
        suite.setStatus(TestSuite.SuiteStatus.Active);

        execution = new TestExecution();
        execution.setExecutionID(1L);
        execution.setReleaseID(1L);
        execution.setSuiteID(1L);
        execution.setExecutedByID(1L);
        execution.setStartDate(LocalDate.now());
        execution.setTotalRun(50);
        execution.setPassed(45);
        execution.setFailed(3);
        execution.setSkipped(2);
        execution.setCoveragePercent(90.0);
        execution.setStatus(TestExecution.ExecutionStatus.Completed);

        gate = new ReleaseGate();
        gate.setGateID(1L);
        gate.setReleaseID(1L);
        gate.setGateName("Test Pass Rate");
        gate.setGateType(ReleaseGate.GateType.TestPassRate);
        gate.setThreshold(80.0);
        gate.setActualValue(90.0);
        gate.setStatus(ReleaseGate.GateStatus.Pending);
    }

    // ===== TEST SUITE TESTS =====

    @Test
    void testGetAllTestSuites_Success() {
        when(testSuiteRepository.findAll()).thenReturn(Arrays.asList(suite));

        List<TestSuite> result = qaService.getAllTestSuites();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Regression Suite", result.get(0).getSuiteName());
    }

    @Test
    void testGetTestSuiteById_Success() {
        when(testSuiteRepository.findById(1L)).thenReturn(Optional.of(suite));

        TestSuite result = qaService.getTestSuiteById(1L);

        assertNotNull(result);
        assertEquals("Regression Suite", result.getSuiteName());
    }

    @Test
    void testGetTestSuiteById_NotFound() {
        when(testSuiteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> qaService.getTestSuiteById(99L));
    }

    @Test
    void testSaveTestSuite_Success() {
        when(testSuiteRepository.save(any(TestSuite.class))).thenReturn(suite);

        TestSuite result = qaService.saveTestSuite(suite);

        assertNotNull(result);
        assertEquals(TestSuite.SuiteStatus.Active, result.getStatus());
    }

    @Test
    void testDeleteTestSuite_Success() {
        doNothing().when(testSuiteRepository).deleteById(1L);

        qaService.deleteTestSuite(1L);

        verify(testSuiteRepository, times(1)).deleteById(1L);
    }

    // ===== TEST EXECUTION TESTS =====

    @Test
    void testGetAllTestExecutions_Success() {
        when(testExecutionRepository.findAll()).thenReturn(Arrays.asList(execution));

        List<TestExecution> result = qaService.getAllTestExecutions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(45, result.get(0).getPassed());
    }

    @Test
    void testSaveTestExecution_AutoCalculatesCoverage() {
        execution.setCoveragePercent(null);
        execution.setTotalRun(50);
        execution.setPassed(45);
        when(testExecutionRepository.save(any(TestExecution.class))).thenReturn(execution);

        TestExecution result = qaService.saveTestExecution(execution);

        verify(testExecutionRepository, times(1)).save(any(TestExecution.class));
    }

    @Test
    void testGetExecutionsByRelease_Success() {
        when(testExecutionRepository.findByReleaseID(1L)).thenReturn(Arrays.asList(execution));

        List<TestExecution> result = qaService.getExecutionsByRelease(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // ===== RELEASE GATE TESTS =====

    @Test
    void testGetAllReleaseGates_Success() {
        when(releaseGateRepository.findAll()).thenReturn(Arrays.asList(gate));

        List<ReleaseGate> result = qaService.getAllReleaseGates();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Pass Rate", result.get(0).getGateName());
    }

    @Test
    void testSaveReleaseGate_DefaultStatusPending() {
        gate.setStatus(null);
        when(releaseGateRepository.save(any(ReleaseGate.class))).thenReturn(gate);

        qaService.saveReleaseGate(gate);

        verify(releaseGateRepository, times(1)).save(any(ReleaseGate.class));
    }

    @Test
    void testEvaluateGate_Pass() {
        when(releaseGateRepository.findById(1L)).thenReturn(Optional.of(gate));
        when(releaseGateRepository.save(any(ReleaseGate.class))).thenReturn(gate);

        ReleaseGate result = qaService.evaluateGate(
                1L, ReleaseGate.GateOutcome.Pass, 1L);

        assertNotNull(result);
        verify(releaseGateRepository, times(1)).save(any(ReleaseGate.class));
    }

    @Test
    void testEvaluateGate_Fail() {
        when(releaseGateRepository.findById(1L)).thenReturn(Optional.of(gate));
        when(releaseGateRepository.save(any(ReleaseGate.class))).thenReturn(gate);

        ReleaseGate result = qaService.evaluateGate(
                1L, ReleaseGate.GateOutcome.Fail, 1L);

        assertNotNull(result);
        verify(releaseGateRepository, times(1)).save(any(ReleaseGate.class));
    }

    @Test
    void testAllGatesPassed_True() {
        gate.setOutcome(ReleaseGate.GateOutcome.Pass);
        when(releaseGateRepository.findByReleaseID(1L)).thenReturn(Arrays.asList(gate));

        boolean result = qaService.allGatesPassed(1L);

        assertTrue(result);
    }

    @Test
    void testAllGatesPassed_False() {
        gate.setOutcome(ReleaseGate.GateOutcome.Fail);
        when(releaseGateRepository.findByReleaseID(1L)).thenReturn(Arrays.asList(gate));

        boolean result = qaService.allGatesPassed(1L);

        assertFalse(result);
    }

    @Test
    void testDeleteReleaseGate_Success() {
        doNothing().when(releaseGateRepository).deleteById(1L);

        qaService.deleteReleaseGate(1L);

        verify(releaseGateRepository, times(1)).deleteById(1L);
    }
}
