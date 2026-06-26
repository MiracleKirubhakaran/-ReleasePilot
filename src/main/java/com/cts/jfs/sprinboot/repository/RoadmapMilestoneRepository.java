package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.RoadmapMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoadmapMilestoneRepository extends JpaRepository<RoadmapMilestone, Long> {

    // ===== SELECT QUERIES =====

    // Find milestones by product
    List<RoadmapMilestone> findByProductID(Long productID);

    // Find milestones by status
    List<RoadmapMilestone> findByStatus(RoadmapMilestone.MilestoneStatus status);

    // Find milestones by target quarter
    List<RoadmapMilestone> findByTargetQuarter(String targetQuarter);

    // Find milestones by product and status
    List<RoadmapMilestone> findByProductIDAndStatus(Long productID,
                                                     RoadmapMilestone.MilestoneStatus status);

    // Find milestones by strategic theme
    List<RoadmapMilestone> findByStrategicTheme(String strategicTheme);

    // Find milestones by product and quarter
    List<RoadmapMilestone> findByProductIDAndTargetQuarter(Long productID, String targetQuarter);

    // Custom query: find all non-cancelled milestones for a product
    @Query("SELECT m FROM RoadmapMilestone m WHERE m.productID = :productID " +
           "AND m.status != 'Cancelled' ORDER BY m.targetQuarter ASC")
    List<RoadmapMilestone> findActiveMilestonesByProduct(@Param("productID") Long productID);

    // ===== UPDATE QUERIES =====

    // Update milestone status
    @Modifying
    @Transactional
    @Query("UPDATE RoadmapMilestone m SET m.status = :status WHERE m.milestoneID = :id")
    int updateMilestoneStatus(@Param("id") Long id,
                               @Param("status") RoadmapMilestone.MilestoneStatus status);

    // Defer all planned milestones of a product
    @Modifying
    @Transactional
    @Query("UPDATE RoadmapMilestone m SET m.status = 'Deferred' " +
           "WHERE m.productID = :productID AND m.status = 'Planned'")
    int deferAllPlannedMilestones(@Param("productID") Long productID);

    // ===== DELETE QUERIES =====

    // Delete all cancelled milestones of a product
    @Modifying
    @Transactional
    @Query("DELETE FROM RoadmapMilestone m WHERE m.productID = :productID " +
           "AND m.status = 'Cancelled'")
    int deleteCancelledMilestones(@Param("productID") Long productID);
}
