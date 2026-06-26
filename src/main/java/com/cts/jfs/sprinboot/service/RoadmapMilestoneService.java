package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.RoadmapMilestone;
import com.cts.jfs.sprinboot.repository.RoadmapMilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.cts.jfs.sprinboot.exception.ResourceNotFoundException;
import com.cts.jfs.sprinboot.exception.DuplicateRecordException;
import com.cts.jfs.sprinboot.exception.InvalidOperationException;
import com.cts.jfs.sprinboot.exception.UnauthorizedException;
@Service
public class RoadmapMilestoneService {

    @Autowired
    private RoadmapMilestoneRepository milestoneRepository;

    // Get all milestones
    public List<RoadmapMilestone> getAllMilestones() {
        return milestoneRepository.findAll();
    }

    // Get milestone by ID
    public RoadmapMilestone getMilestoneById(Long id) {
        return milestoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Milestone not found with ID: " + id));
    }

    // Save new milestone
    public RoadmapMilestone saveMilestone(RoadmapMilestone milestone) {
        if (milestone.getStatus() == null) {
            milestone.setStatus(RoadmapMilestone.MilestoneStatus.Planned);
        }
        return milestoneRepository.save(milestone);
    }

    // Update milestone
    public RoadmapMilestone updateMilestone(Long id, RoadmapMilestone updated) {
        RoadmapMilestone existing = getMilestoneById(id);
        existing.setMilestoneName(updated.getMilestoneName());
        existing.setTargetQuarter(updated.getTargetQuarter());
        existing.setStrategicTheme(updated.getStrategicTheme());
        existing.setExpectedVersion(updated.getExpectedVersion());
        existing.setStatus(updated.getStatus());
        return milestoneRepository.save(existing);
    }

    // Delete milestone
    public void deleteMilestone(Long id) {
        milestoneRepository.deleteById(id);
    }

    // Get milestones by product
    public List<RoadmapMilestone> getMilestonesByProduct(Long productId) {
        return milestoneRepository.findByProductID(productId);
    }

    // Get milestones by status
    public List<RoadmapMilestone> getMilestonesByStatus(RoadmapMilestone.MilestoneStatus status) {
        return milestoneRepository.findByStatus(status);
    }

    // Get milestones by quarter
    public List<RoadmapMilestone> getMilestonesByQuarter(String quarter) {
        return milestoneRepository.findByTargetQuarter(quarter);
    }
}
