package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditID;

    private Long userID;

    private String action;

    private String entityType;

    private Long recordID;

    private LocalDateTime timestamp;


	public Long getAuditID() {
		return auditID;
	}

	public void setAuditID(Long auditID) {
		this.auditID = auditID;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getRecordID() {
		return recordID;
	}

	public void setRecordID(Long recordID) {
		this.recordID = recordID;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

    public AuditLog() {
    }

    public AuditLog(Long auditID, Long userID, String action, String entityType, Long recordID, LocalDateTime timestamp) {
        this.auditID = auditID;
        this.userID = userID;
        this.action = action;
        this.entityType = entityType;
        this.recordID = recordID;
        this.timestamp = timestamp;
    }
}
