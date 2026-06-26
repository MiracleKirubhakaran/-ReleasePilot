package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.CustomerAdvisory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerAdvisoryRepository extends JpaRepository<CustomerAdvisory, Long> {

    // ===== SELECT QUERIES =====

    // Find advisories by release ID
    List<CustomerAdvisory> findByReleaseID(Long releaseID);

    // Find advisories by severity
    List<CustomerAdvisory> findBySeverity(CustomerAdvisory.Severity severity);

    // Find advisories by status
    List<CustomerAdvisory> findByStatus(CustomerAdvisory.AdvisoryStatus status);

    // Find advisories by release and severity
    List<CustomerAdvisory> findByReleaseIDAndSeverity(Long releaseID,
                                                       CustomerAdvisory.Severity severity);

    // Find advisories published after a date
    List<CustomerAdvisory> findByPublishedDateAfter(LocalDate date);

    // Find advisories published between dates
    List<CustomerAdvisory> findByPublishedDateBetween(LocalDate from, LocalDate to);

    // Find by severity and status
    List<CustomerAdvisory> findBySeverityAndStatus(CustomerAdvisory.Severity severity,
                                                    CustomerAdvisory.AdvisoryStatus status);

    // Find advisories by title keyword
    List<CustomerAdvisory> findByTitleContaining(String keyword);

    // Custom query: all active critical advisories
    @Query("SELECT a FROM CustomerAdvisory a WHERE a.severity = 'Critical' " +
           "AND a.status = 'Published' ORDER BY a.publishedDate DESC")
    List<CustomerAdvisory> findActiveCriticalAdvisories();

    // ===== UPDATE QUERIES =====

    // Update advisory status
    @Modifying
    @Transactional
    @Query("UPDATE CustomerAdvisory a SET a.status = :status WHERE a.advisoryID = :id")
    int updateAdvisoryStatus(@Param("id") Long id,
                              @Param("status") CustomerAdvisory.AdvisoryStatus status);

    // Publish an advisory
    @Modifying
    @Transactional
    @Query("UPDATE CustomerAdvisory a SET a.status = 'Published', a.publishedDate = :date " +
           "WHERE a.advisoryID = :id")
    int publishAdvisory(@Param("id") Long id, @Param("date") LocalDate date);

    // Archive an advisory
    @Modifying
    @Transactional
    @Query("UPDATE CustomerAdvisory a SET a.status = 'Archived' WHERE a.advisoryID = :id")
    int archiveAdvisory(@Param("id") Long id);

    // ===== DELETE QUERIES =====

    // Delete all archived advisories for a release
    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerAdvisory a WHERE a.releaseID = :releaseID " +
           "AND a.status = 'Archived'")
    int deleteArchivedByRelease(@Param("releaseID") Long releaseID);
}
