package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.BacklogItem;
import com.cts.jfs.sprinboot.repository.BacklogItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BacklogItemService {

    @Autowired
    private BacklogItemRepository backlogItemRepository;

    // Get all backlog items
    public List<BacklogItem> getAllBacklogItems() {
        return backlogItemRepository.findAll();
    }

    // Get backlog item by ID
    public BacklogItem getBacklogItemById(Long id) {
        return backlogItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BacklogItem not found with ID: " + id));
    }

    // Save new backlog item
    public BacklogItem saveBacklogItem(BacklogItem item) {
        if (item.getStatus() == null) {
            item.setStatus(BacklogItem.ItemStatus.New);
        }
        return backlogItemRepository.save(item);
    }

    // Update backlog item
    public BacklogItem updateBacklogItem(Long id, BacklogItem updated) {
        BacklogItem existing = getBacklogItemById(id);
        existing.setTitle(updated.getTitle());
        existing.setType(updated.getType());
        existing.setPriority(updated.getPriority());
        existing.setStoryPoints(updated.getStoryPoints());
        existing.setRequestedByID(updated.getRequestedByID());
        existing.setMilestoneID(updated.getMilestoneID());
        existing.setStatus(updated.getStatus());
        return backlogItemRepository.save(existing);
    }

    // Delete backlog item
    public void deleteBacklogItem(Long id) {
        backlogItemRepository.deleteById(id);
    }

    // Get backlog by product
    public List<BacklogItem> getBacklogByProduct(Long productId) {
        return backlogItemRepository.findByProductID(productId);
    }

    // Get backlog by priority
    public List<BacklogItem> getByPriority(BacklogItem.Priority priority) {
        return backlogItemRepository.findByPriority(priority);
    }

    // Get backlog by status
    public List<BacklogItem> getByStatus(BacklogItem.ItemStatus status) {
        return backlogItemRepository.findByStatus(status);
    }

    // Get backlog by type
    public List<BacklogItem> getByType(BacklogItem.ItemType type) {
        return backlogItemRepository.findByType(type);
    }

    // Get backlog by milestone
    public List<BacklogItem> getByMilestone(Long milestoneId) {
        return backlogItemRepository.findByMilestoneID(milestoneId);
    }
}
