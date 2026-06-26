package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patch_dependency")
@Builder
public class PatchDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dependencyID;

    private Long releaseID;

    private Long dependsOnReleaseID;

    @Enumerated(EnumType.STRING)
    private DependencyType dependencyType;

    private String notes;


	public enum DependencyType {
        MustInstallBefore, OptionalPrerequisite
    }

	public Long getDependencyID() {
		return dependencyID;
	}

	public void setDependencyID(Long dependencyID) {
		this.dependencyID = dependencyID;
	}

	public Long getReleaseID() {
		return releaseID;
	}

	public void setReleaseID(Long releaseID) {
		this.releaseID = releaseID;
	}

	public Long getDependsOnReleaseID() {
		return dependsOnReleaseID;
	}

	public void setDependsOnReleaseID(Long dependsOnReleaseID) {
		this.dependsOnReleaseID = dependsOnReleaseID;
	}

	public DependencyType getDependencyType() {
		return dependencyType;
	}

	public void setDependencyType(DependencyType dependencyType) {
		this.dependencyType = dependencyType;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

    public PatchDependency(Long dependencyID, Long releaseID, Long dependsOnReleaseID, DependencyType dependencyType, String notes) {
        this.dependencyID = dependencyID;
        this.releaseID = releaseID;
        this.dependsOnReleaseID = dependsOnReleaseID;
        this.dependencyType = dependencyType;
        this.notes = notes;
    }

    public PatchDependency() {
    }
}
