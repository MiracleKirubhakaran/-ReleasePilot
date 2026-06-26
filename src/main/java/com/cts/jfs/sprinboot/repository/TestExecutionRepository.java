package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.TestExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TestExecutionRepository extends JpaRepository<TestExecution, Long> {

    // ===== SELECT QUERIES =====

    // Find executions by release ID
    List<TestExecution> findByReleaseID(Long releaseID);

    // Find executions by suite ID
    List<TestExecution> findBySuiteID(Long suiteID);

    // Find executions by executed user
    List<TestExecution> findByExecutedByID(Long executedByID);

    // Find executions by status
    List<TestExecution> findByStatus(TestExecution.ExecutionStatus status);

    // Find executions by release and suite
    List<TestExecution> findByReleaseIDAndSuiteID(Long releaseID, Long suiteID);

    // Find executions by release and status
    List<TestExecution> findByReleaseIDAndStatus(Long releaseID,
                                                  TestExecution.ExecutionStatus status);

    // Custom query: average coverage for a release
    @Query("SELECT AVG(te.coveragePercent) FROM TestExecution te WHERE te.releaseID = :releaseID " +
           "AND te.status = 'Completed'")
    Double getAverageCoverageByRelease(@Param("releaseID") Long releaseID);

    // Custom query: total passed tests for a release
    @Query("SELECT SUM(te.passed) FROM TestExecution te WHERE te.releaseID = :releaseID")
    Integer getTotalPassedByRelease(@Param("releaseID") Long releaseID);

    // Custom query: total failed tests for a release
    @Query("SELECT SUM(te.failed) FROM TestExecution te WHERE te.releaseID = :releaseID")
    Integer getTotalFailedByRelease(@Param("releaseID") Long releaseID);

    // ===== UPDATE QUERIES =====

    // Update execution status
    @Modifying
    @Transactional
    @Query("UPDATE TestExecution te SET te.status = :status WHERE te.executionID = :id")
    int updateExecutionStatus(@Param("id") Long id,
                               @Param("status") TestExecution.ExecutionStatus status);

    // Update test results for an execution
    @Modifying
    @Transactional
    @Query("UPDATE TestExecution te SET te.passed = :passed, te.failed = :failed, " +
           "te.skipped = :skipped, te.totalRun = :totalRun, " +
           "te.coveragePercent = :coverage WHERE te.executionID = :id")
    int updateTestResults(@Param("id") Long id,
                           @Param("passed") Integer passed,
                           @Param("failed") Integer failed,
                           @Param("skipped") Integer skipped,
                           @Param("totalRun") Integer totalRun,
                           @Param("coverage") Double coverage);

    // Abort an in-progress execution
    @Modifying
    @Transactional
    @Query("UPDATE TestExecution te SET te.status = 'Aborted' WHERE te.executionID = :id " +
           "AND te.status = 'InProgress'")
    int abortExecution(@Param("id") Long id);

    // ===== DELETE QUERIES =====

    // Delete all aborted executions for a release
    @Modifying
    @Transactional
    @Query("DELETE FROM TestExecution te WHERE te.releaseID = :releaseID " +
           "AND te.status = 'Aborted'")
    int deleteAbortedExecutions(@Param("releaseID") Long releaseID);
}
