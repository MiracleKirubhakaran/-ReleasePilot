package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "release_gate")
@Builder
public class ReleaseGate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gateID;

    private Long releaseID;

    private String gateName;

    @Enumerated(EnumType.STRING)
    private GateType gateType;

    private Double threshold;

    private Double actualValue;

    @Enumerated(EnumType.STRING)
    private GateOutcome outcome;

    private Long evaluatedByID;

    @Enumerated(EnumType.STRING)
    private GateStatus status;

    public enum GateType {
        TestPassRate, SecurityScan, PerformanceBenchmark, ManualApproval
    }

    public enum GateOutcome {
        Pass, Fail, Override
    }

    public enum GateStatus {
        Pending, Evaluated
    }


	public Long getGateID() {
		return gateID;
	}

	public void setGateID(Long gateID) {
		this.gateID = gateID;
	}

	public Long getReleaseID() {
		return releaseID;
	}

	public void setReleaseID(Long releaseID) {
		this.releaseID = releaseID;
	}

	public String getGateName() {
		return gateName;
	}

	public void setGateName(String gateName) {
		this.gateName = gateName;
	}

	public GateType getGateType() {
		return gateType;
	}

	public void setGateType(GateType gateType) {
		this.gateType = gateType;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public Double getActualValue() {
		return actualValue;
	}

	public void setActualValue(Double actualValue) {
		this.actualValue = actualValue;
	}

	public GateOutcome getOutcome() {
		return outcome;
	}

	public void setOutcome(GateOutcome outcome) {
		this.outcome = outcome;
	}

	public Long getEvaluatedByID() {
		return evaluatedByID;
	}

	public void setEvaluatedByID(Long evaluatedByID) {
		this.evaluatedByID = evaluatedByID;
	}

	public GateStatus getStatus() {
		return status;
	}

	public void setStatus(GateStatus status) {
		this.status = status;
	}

    public ReleaseGate() {
    }

    public ReleaseGate(Long gateID, Long releaseID, String gateName, GateType gateType, Double threshold, Double actualValue, GateOutcome outcome, Long evaluatedByID, GateStatus status) {
        this.gateID = gateID;
        this.releaseID = releaseID;
        this.gateName = gateName;
        this.gateType = gateType;
        this.threshold = threshold;
        this.actualValue = actualValue;
        this.outcome = outcome;
        this.evaluatedByID = evaluatedByID;
        this.status = status;
    }
}
