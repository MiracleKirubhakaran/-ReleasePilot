package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "backlog_item")
@Builder
public class BacklogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemID;

    private Long productID;

    private String title;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private Integer storyPoints;

    private Long requestedByID;

    private Long milestoneID;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    public enum ItemType {
        Feature, BugFix, TechnicalDebt, SecurityPatch, Enhancement
    }

    public enum Priority {
        Critical, High, Medium, Low
    }

    public enum ItemStatus {
        New, Groomed, Planned, InDevelopment, Done, Deferred, Cancelled
    }


	public Long getItemID() {
		return itemID;
	}

	public void setItemID(Long itemID) {
		this.itemID = itemID;
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Integer getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(Integer storyPoints) {
		this.storyPoints = storyPoints;
	}

	public Long getRequestedByID() {
		return requestedByID;
	}

	public void setRequestedByID(Long requestedByID) {
		this.requestedByID = requestedByID;
	}

	public Long getMilestoneID() {
		return milestoneID;
	}

	public void setMilestoneID(Long milestoneID) {
		this.milestoneID = milestoneID;
	}

	public ItemStatus getStatus() {
		return status;
	}

	public void setStatus(ItemStatus status) {
		this.status = status;
	}

    public BacklogItem(Long itemID, Long productID, String title, ItemType type, Priority priority, Integer storyPoints, Long requestedByID, Long milestoneID, ItemStatus status) {
        this.itemID = itemID;
        this.productID = productID;
        this.title = title;
        this.type = type;
        this.priority = priority;
        this.storyPoints = storyPoints;
        this.requestedByID = requestedByID;
        this.milestoneID = milestoneID;
        this.status = status;
    }

    public BacklogItem() {
    }
}
