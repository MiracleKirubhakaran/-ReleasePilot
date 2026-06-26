package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.TestSuite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TestSuiteRepository extends JpaRepository<TestSuite, Long> {

    // ===== SELECT QUERIES =====

    // Find test suites by product
    List<TestSuite> findByProductID(Long productID);

    // Find test suites by type
    List<TestSuite> findByType(TestSuite.SuiteType type);

    // Find test suites by status
    List<TestSuite> findByStatus(TestSuite.SuiteStatus status);

    // Find test suites by product and type
    List<TestSuite> findByProductIDAndType(Long productID, TestSuite.SuiteType type);

    // Find test suites by product and status
    List<TestSuite> findByProductIDAndStatus(Long productID, TestSuite.SuiteStatus status);

    // Find by suite name containing keyword
    List<TestSuite> findBySuiteNameContaining(String keyword);

    // Custom query: count active suites per product
    @Query("SELECT COUNT(t) FROM TestSuite t WHERE t.productID = :productID " +
           "AND t.status = 'Active'")
    Long countActiveSuitesByProduct(@Param("productID") Long productID);

    // ===== UPDATE QUERIES =====

    // Update test suite status
    @Modifying
    @Transactional
    @Query("UPDATE TestSuite t SET t.status = :status WHERE t.suiteID = :id")
    int updateTestSuiteStatus(@Param("id") Long id,
                               @Param("status") TestSuite.SuiteStatus status);

    // Update total test cases count
    @Modifying
    @Transactional
    @Query("UPDATE TestSuite t SET t.totalTestCases = :count WHERE t.suiteID = :id")
    int updateTotalTestCases(@Param("id") Long id, @Param("count") Integer count);

    // Archive all suites of a product
    @Modifying
    @Transactional
    @Query("UPDATE TestSuite t SET t.status = 'Archived' WHERE t.productID = :productID")
    int archiveAllSuitesByProduct(@Param("productID") Long productID);

    // ===== DELETE QUERIES =====

    // Delete all archived suites for a product
    @Modifying
    @Transactional
    @Query("DELETE FROM TestSuite t WHERE t.productID = :productID AND t.status = 'Archived'")
    int deleteArchivedSuites(@Param("productID") Long productID);
}
