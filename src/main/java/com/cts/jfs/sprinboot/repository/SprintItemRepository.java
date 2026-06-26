package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.SprintItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SprintItemRepository extends JpaRepository<SprintItem, Long> {

    // ===== SELECT QUERIES =====

    // Find sprint items by sprint ID
    List<SprintItem> findBySprintID(Long sprintID);

    // Find sprint items by backlog item ID
    List<SprintItem> findByBacklogItemID(Long backlogItemID);

    // Find sprint items by assigned developer
    List<SprintItem> findByAssignedDevID(Long assignedDevID);

    // Find sprint items by status
    List<SprintItem> findByStatus(SprintItem.SprintItemStatus status);

    // Find sprint items by sprint and status
    List<SprintItem> findBySprintIDAndStatus(Long sprintID, SprintItem.SprintItemStatus status);

    // Custom query: total estimated hours in a sprint
    @Query("SELECT SUM(si.estimatedHours) FROM SprintItem si WHERE si.sprintID = :sprintID")
    Double getTotalEstimatedHours(@Param("sprintID") Long sprintID);

    // Custom query: total actual hours in a sprint
    @Query("SELECT SUM(si.actualHours) FROM SprintItem si WHERE si.sprintID = :sprintID")
    Double getTotalActualHours(@Param("sprintID") Long sprintID);

    // Custom query: find all blocked items assigned to a dev
    @Query("SELECT si FROM SprintItem si WHERE si.assignedDevID = :devID " +
           "AND si.status = 'Blocked'")
    List<SprintItem> findBlockedItemsByDev(@Param("devID") Long devID);

    // ===== UPDATE QUERIES =====

    // Update sprint item status
    @Modifying
    @Transactional
    @Query("UPDATE SprintItem si SET si.status = :status WHERE si.sprintItemID = :id")
    int updateSprintItemStatus(@Param("id") Long id,
                                @Param("status") SprintItem.SprintItemStatus status);

    // Update actual hours for a sprint item
    @Modifying
    @Transactional
    @Query("UPDATE SprintItem si SET si.actualHours = :hours WHERE si.sprintItemID = :id")
    int updateActualHours(@Param("id") Long id,
                           @Param("hours") Double hours);

    // Reassign developer for a sprint item
    @Modifying
    @Transactional
    @Query("UPDATE SprintItem si SET si.assignedDevID = :devID WHERE si.sprintItemID = :id")
    int reassignDeveloper(@Param("id") Long id,
                           @Param("devID") Long devID);

    // ===== DELETE QUERIES =====

    // Delete all sprint items of a sprint
    @Modifying
    @Transactional
    @Query("DELETE FROM SprintItem si WHERE si.sprintID = :sprintID")
    int deleteAllItemsInSprint(@Param("sprintID") Long sprintID);
}
