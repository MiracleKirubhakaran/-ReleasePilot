package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "change_freeze_window")
@Builder
public class ChangeFreezeWindow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freezeID;

    private Long productID;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private Long approvedByID;

    @Enumerated(EnumType.STRING)
    private FreezeStatus status;


	public enum FreezeStatus {
        Scheduled, Active, Lifted
    }

	public Long getFreezeID() {
		return freezeID;
	}

	public void setFreezeID(Long freezeID) {
		this.freezeID = freezeID;
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getApprovedByID() {
		return approvedByID;
	}

	public void setApprovedByID(Long approvedByID) {
		this.approvedByID = approvedByID;
	}

	public FreezeStatus getStatus() {
		return status;
	}

	public void setStatus(FreezeStatus status) {
		this.status = status;
	}

    public ChangeFreezeWindow(Long freezeID, Long productID, LocalDate startDate, LocalDate endDate, String reason, Long approvedByID, FreezeStatus status) {
        this.freezeID = freezeID;
        this.productID = productID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.approvedByID = approvedByID;
        this.status = status;
    }

    public ChangeFreezeWindow() {
    }
}
