package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // ===== SELECT QUERIES =====

    // Find logs by user ID
    List<AuditLog> findByUserID(Long userID);

    // Find logs by entity type
    List<AuditLog> findByEntityType(String entityType);

    // Find logs by record ID
    List<AuditLog> findByRecordID(Long recordID);

    // Find logs by user and entity type
    List<AuditLog> findByUserIDAndEntityType(Long userID, String entityType);

    // Find logs within a time range
    List<AuditLog> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

    // Find logs by action keyword
    List<AuditLog> findByActionContaining(String action);

    // Custom query: find latest 20 logs for a user
    @Query("SELECT a FROM AuditLog a WHERE a.userID = :userID ORDER BY a.timestamp DESC")
    List<AuditLog> findRecentLogsByUser(@Param("userID") Long userID);

    // Custom query: find logs for an entity record
    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType " +
           "AND a.recordID = :recordID ORDER BY a.timestamp DESC")
    List<AuditLog> findLogsByEntityAndRecord(@Param("entityType") String entityType,
                                              @Param("recordID") Long recordID);

    // ===== DELETE QUERIES =====

    // Delete all audit logs older than a given timestamp
    @Modifying
    @Transactional
    @Query("DELETE FROM AuditLog a WHERE a.timestamp < :before")
    int deleteLogsOlderThan(@Param("before") LocalDateTime before);

    // Delete all logs for a specific user
    @Modifying
    @Transactional
    @Query("DELETE FROM AuditLog a WHERE a.userID = :userID")
    int deleteLogsByUser(@Param("userID") Long userID);
}
