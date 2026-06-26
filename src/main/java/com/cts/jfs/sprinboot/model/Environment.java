package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "environment")
@Builder
public class Environment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long envID;

    private Long productID;

    @Enumerated(EnumType.STRING)
    private EnvName envName;

    private Long ownerID;

    private String currentVersion;

    private LocalDate lastPromotionDate;

    @Enumerated(EnumType.STRING)
    private EnvStatus status;


	public enum EnvName {
        Dev, SIT, UAT, PreProd, Production
    }

    public enum EnvStatus {
        Active, Frozen, Maintenance
    }

	public Long getEnvID() {
		return envID;
	}

	public void setEnvID(Long envID) {
		this.envID = envID;
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public EnvName getEnvName() {
		return envName;
	}

	public void setEnvName(EnvName envName) {
		this.envName = envName;
	}

	public Long getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public LocalDate getLastPromotionDate() {
		return lastPromotionDate;
	}

	public void setLastPromotionDate(LocalDate lastPromotionDate) {
		this.lastPromotionDate = lastPromotionDate;
	}

	public EnvStatus getStatus() {
		return status;
	}

	public void setStatus(EnvStatus status) {
		this.status = status;
	}

    public Environment(Long envID, Long productID, EnvName envName, Long ownerID, String currentVersion, LocalDate lastPromotionDate, EnvStatus status) {
        this.envID = envID;
        this.productID = productID;
        this.envName = envName;
        this.ownerID = ownerID;
        this.currentVersion = currentVersion;
        this.lastPromotionDate = lastPromotionDate;
        this.status = status;
    }

    public Environment() {
    }
}
