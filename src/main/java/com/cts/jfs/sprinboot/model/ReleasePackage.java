package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "release_package")
@Builder
public class ReleasePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long releaseID;

    @NotNull(message = "Product ID is required")
    private Long productID;

    @NotBlank(message = "Version number is required")
    private String versionNumber;

    @Enumerated(EnumType.STRING)
    private ReleaseType releaseType;

    // Comma-separated backlog item IDs (e.g., "1,2,3")
    private String includedItemIDs;

    @Column(columnDefinition = "TEXT")
    private String releaseNotesDraft;

    private LocalDate targetReleaseDate;

    private Long releasedByID;

    @Enumerated(EnumType.STRING)
    private ReleaseStatus status;

    public enum ReleaseType {
        Major, Minor, Patch, Hotfix
    }

    public enum ReleaseStatus {
        Draft, PackagingComplete, QATesting, SignedOff, Released, Recalled
    }


	public Long getReleaseID() {
		return releaseID;
	}

	public void setReleaseID(Long releaseID) {
		this.releaseID = releaseID;
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public ReleaseType getReleaseType() {
		return releaseType;
	}

	public void setReleaseType(ReleaseType releaseType) {
		this.releaseType = releaseType;
	}

	public String getIncludedItemIDs() {
		return includedItemIDs;
	}

	public void setIncludedItemIDs(String includedItemIDs) {
		this.includedItemIDs = includedItemIDs;
	}

	public String getReleaseNotesDraft() {
		return releaseNotesDraft;
	}

	public void setReleaseNotesDraft(String releaseNotesDraft) {
		this.releaseNotesDraft = releaseNotesDraft;
	}

	public LocalDate getTargetReleaseDate() {
		return targetReleaseDate;
	}

	public void setTargetReleaseDate(LocalDate targetReleaseDate) {
		this.targetReleaseDate = targetReleaseDate;
	}

	public Long getReleasedByID() {
		return releasedByID;
	}

	public void setReleasedByID(Long releasedByID) {
		this.releasedByID = releasedByID;
	}

	public ReleaseStatus getStatus() {
		return status;
	}

	public void setStatus(ReleaseStatus status) {
		this.status = status;
	}

    public ReleasePackage(Long releaseID, Long productID, String versionNumber, ReleaseType releaseType, String includedItemIDs, String releaseNotesDraft, LocalDate targetReleaseDate, Long releasedByID, ReleaseStatus status) {
        this.releaseID = releaseID;
        this.productID = productID;
        this.versionNumber = versionNumber;
        this.releaseType = releaseType;
        this.includedItemIDs = includedItemIDs;
        this.releaseNotesDraft = releaseNotesDraft;
        this.targetReleaseDate = targetReleaseDate;
        this.releasedByID = releasedByID;
        this.status = status;
    }

    public ReleasePackage() {
    }
}
