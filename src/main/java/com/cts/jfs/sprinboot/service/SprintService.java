package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.Sprint;
import com.cts.jfs.sprinboot.model.SprintItem;
import com.cts.jfs.sprinboot.repository.SprintRepository;
import com.cts.jfs.sprinboot.repository.SprintItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.cts.jfs.sprinboot.exception.ResourceNotFoundException;
import com.cts.jfs.sprinboot.exception.DuplicateRecordException;
import com.cts.jfs.sprinboot.exception.InvalidOperationException;
import com.cts.jfs.sprinboot.exception.UnauthorizedException;
@Service
public class SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private SprintItemRepository sprintItemRepository;

    // Get all sprints
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    // Get sprint by ID
    public Sprint getSprintById(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found with ID: " + id));
    }

    // Save new sprint
    public Sprint saveSprint(Sprint sprint) {
        if (sprint.getStatus() == null) {
            sprint.setStatus(Sprint.SprintStatus.Planned);
        }
        if (sprint.getCompletedPoints() == null) {
            sprint.setCompletedPoints(0);
        }
        return sprintRepository.save(sprint);
    }

    // Update sprint
    public Sprint updateSprint(Long id, Sprint updated) {
        Sprint existing = getSprintById(id);
        existing.setSprintName(updated.getSprintName());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setGoal(updated.getGoal());
        existing.setTotalStoryPoints(updated.getTotalStoryPoints());
        existing.setCompletedPoints(updated.getCompletedPoints());
        existing.setStatus(updated.getStatus());
        return sprintRepository.save(existing);
    }

    // Delete sprint
    public void deleteSprint(Long id) {
        sprintRepository.deleteById(id);
    }

    // Get sprints by product
    public List<Sprint> getSprintsByProduct(Long productId) {
        return sprintRepository.findByProductID(productId);
    }

    // Get sprints by status
    public List<Sprint> getSprintsByStatus(Sprint.SprintStatus status) {
        return sprintRepository.findByStatus(status);
    }

    // Get active sprint for product
    public Sprint getActiveSprintByProduct(Long productId) {
        return sprintRepository.findByProductIDAndStatus(productId, Sprint.SprintStatus.Active)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No active sprint found for product: " + productId));
    }

    // ===== SPRINT ITEMS =====

    // Add item to sprint
    public SprintItem addItemToSprint(SprintItem sprintItem) {
        if (sprintItem.getStatus() == null) {
            sprintItem.setStatus(SprintItem.SprintItemStatus.ToDo);
        }
        return sprintItemRepository.save(sprintItem);
    }

    // Get sprint items by sprint ID
    public List<SprintItem> getSprintItemsBySprintId(Long sprintId) {
        return sprintItemRepository.findBySprintID(sprintId);
    }

    // Update sprint item status
    public SprintItem updateSprintItemStatus(Long sprintItemId, SprintItem.SprintItemStatus status) {
        SprintItem item = sprintItemRepository.findById(sprintItemId)
                .orElseThrow(() -> new RuntimeException("SprintItem not found with ID: " + sprintItemId));
        item.setStatus(status);
        return sprintItemRepository.save(item);
    }

    // Remove item from sprint
    public void removeSprintItem(Long sprintItemId) {
        sprintItemRepository.deleteById(sprintItemId);
    }
}
