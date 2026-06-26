package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "release_note")
@Builder
public class ReleaseNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteID;

    private Long releaseID;

    private String versionNumber;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String newFeatures;

    @Column(columnDefinition = "TEXT")
    private String bugFixes;

    @Column(columnDefinition = "TEXT")
    private String knownIssues;

    @Column(columnDefinition = "TEXT")
    private String deprecations;

    @Column(columnDefinition = "TEXT")
    private String upgradeInstructions;

    private Long authoredByID;

    @Enumerated(EnumType.STRING)
    private NoteStatus status;

    public enum NoteStatus {
        Draft, Review, Approved, Published
    }

	public Long getNoteID() {
		return noteID;
	}

	public void setNoteID(Long noteID) {
		this.noteID = noteID;
	}

	public Long getReleaseID() {
		return releaseID;
	}

	public void setReleaseID(Long releaseID) {
		this.releaseID = releaseID;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getNewFeatures() {
		return newFeatures;
	}

	public void setNewFeatures(String newFeatures) {
		this.newFeatures = newFeatures;
	}

	public String getBugFixes() {
		return bugFixes;
	}

	public void setBugFixes(String bugFixes) {
		this.bugFixes = bugFixes;
	}

	public String getKnownIssues() {
		return knownIssues;
	}

	public void setKnownIssues(String knownIssues) {
		this.knownIssues = knownIssues;
	}

	public String getDeprecations() {
		return deprecations;
	}

	public void setDeprecations(String deprecations) {
		this.deprecations = deprecations;
	}

	public String getUpgradeInstructions() {
		return upgradeInstructions;
	}

	public void setUpgradeInstructions(String upgradeInstructions) {
		this.upgradeInstructions = upgradeInstructions;
	}

	public Long getAuthoredByID() {
		return authoredByID;
	}

	public void setAuthoredByID(Long authoredByID) {
		this.authoredByID = authoredByID;
	}

	public NoteStatus getStatus() {
		return status;
	}

	public void setStatus(NoteStatus status) {
		this.status = status;
	}

    public ReleaseNote(Long noteID, Long releaseID, String versionNumber, String summary, String newFeatures, String bugFixes, String knownIssues, String deprecations, String upgradeInstructions, Long authoredByID, NoteStatus status) {
        this.noteID = noteID;
        this.releaseID = releaseID;
        this.versionNumber = versionNumber;
        this.summary = summary;
        this.newFeatures = newFeatures;
        this.bugFixes = bugFixes;
        this.knownIssues = knownIssues;
        this.deprecations = deprecations;
        this.upgradeInstructions = upgradeInstructions;
        this.authoredByID = authoredByID;
        this.status = status;
    }

    public ReleaseNote() {
    }

}
