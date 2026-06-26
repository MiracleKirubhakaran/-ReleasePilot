package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.AuditLog;
import com.cts.jfs.sprinboot.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class AuditLogService {

	//this will inject a proxy object of class which implement JpaReposiotory internally
    @Autowired
    private AuditLogRepository auditLogRepository;

    // Get all audit logs
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    // Get audit log by ID
    public AuditLog getAuditLogById(Long id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuditLog not found with ID: " + id));
    }

    // Log an action (called internally from other services)
    public void logAction(Long userId, String action, String entityType, Long recordId) {
        AuditLog log = new AuditLog();
        log.setUserID(userId);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setRecordID(recordId);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    // Get logs by user
    public List<AuditLog> getAuditLogsByUser(Long userId) {
        return auditLogRepository.findByUserID(userId);
    }

    // Get logs by entity type
    public List<AuditLog> getAuditLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType);
    }

    // Get logs by record ID
    public List<AuditLog> getAuditLogsByRecordId(Long recordId) {
        return auditLogRepository.findByRecordID(recordId);
    }

    // Get logs between timestamps
    public List<AuditLog> getAuditLogsBetween(LocalDateTime from, LocalDateTime to) {
        return auditLogRepository.findByTimestampBetween(from, to);
    }
}
