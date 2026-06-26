package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotion_request")
@Builder
public class PromotionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionID;

    private Long releaseID;

    private Long fromEnvID;

    private Long toEnvID;

    private Long requestedByID;

    private Long approvedByID;

    private LocalDateTime scheduledDateTime;

    private LocalDateTime actualDateTime;

    @Enumerated(EnumType.STRING)
    private PromotionStatus status;

    public enum PromotionStatus {
        Pending, Approved, InProgress, Completed, RolledBack, Rejected
    }


	public Long getPromotionID() {
		return promotionID;
	}

	public void setPromotionID(Long promotionID) {
		this.promotionID = promotionID;
	}

	public Long getReleaseID() {
		return releaseID;
	}

	public void setReleaseID(Long releaseID) {
		this.releaseID = releaseID;
	}

	public Long getFromEnvID() {
		return fromEnvID;
	}

	public void setFromEnvID(Long fromEnvID) {
		this.fromEnvID = fromEnvID;
	}

	public Long getToEnvID() {
		return toEnvID;
	}

	public void setToEnvID(Long toEnvID) {
		this.toEnvID = toEnvID;
	}

	public Long getRequestedByID() {
		return requestedByID;
	}

	public void setRequestedByID(Long requestedByID) {
		this.requestedByID = requestedByID;
	}

	public Long getApprovedByID() {
		return approvedByID;
	}

	public void setApprovedByID(Long approvedByID) {
		this.approvedByID = approvedByID;
	}

	public LocalDateTime getScheduledDateTime() {
		return scheduledDateTime;
	}

	public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
		this.scheduledDateTime = scheduledDateTime;
	}

	public LocalDateTime getActualDateTime() {
		return actualDateTime;
	}

	public void setActualDateTime(LocalDateTime actualDateTime) {
		this.actualDateTime = actualDateTime;
	}

	public PromotionStatus getStatus() {
		return status;
	}

	public void setStatus(PromotionStatus status) {
		this.status = status;
	}

    public PromotionRequest() {
    }

    public PromotionRequest(Long promotionID, Long releaseID, Long fromEnvID, Long toEnvID, Long requestedByID, Long approvedByID, LocalDateTime scheduledDateTime, LocalDateTime actualDateTime, PromotionStatus status) {
        this.promotionID = promotionID;
        this.releaseID = releaseID;
        this.fromEnvID = fromEnvID;
        this.toEnvID = toEnvID;
        this.requestedByID = requestedByID;
        this.approvedByID = approvedByID;
        this.scheduledDateTime = scheduledDateTime;
        this.actualDateTime = actualDateTime;
        this.status = status;
    }
}
