package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.BacklogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BacklogItemRepository extends JpaRepository<BacklogItem, Long> {

    // ===== SELECT QUERIES =====

    // Find by product
    List<BacklogItem> findByProductID(Long productID);

    // Find by priority
    List<BacklogItem> findByPriority(BacklogItem.Priority priority);

    // Find by status
    List<BacklogItem> findByStatus(BacklogItem.ItemStatus status);

    // Find by type
    List<BacklogItem> findByType(BacklogItem.ItemType type);

    // Find by milestone
    List<BacklogItem> findByMilestoneID(Long milestoneID);

    // Find by product and status
    List<BacklogItem> findByProductIDAndStatus(Long productID, BacklogItem.ItemStatus status);

    // Find by product and priority
    List<BacklogItem> findByProductIDAndPriority(Long productID, BacklogItem.Priority priority);

    // Find by requested user
    List<BacklogItem> findByRequestedByID(Long requestedByID);

    // Custom query: get items by product ordered by priority
    @Query("SELECT b FROM BacklogItem b WHERE b.productID = :productID " +
           "ORDER BY b.priority ASC")
    List<BacklogItem> findByProductIDOrderByPriority(@Param("productID") Long productID);

    // Custom query: count items by product and status
    @Query("SELECT COUNT(b) FROM BacklogItem b WHERE b.productID = :productID " +
           "AND b.status = :status")
    Long countByProductAndStatus(@Param("productID") Long productID,
                                  @Param("status") BacklogItem.ItemStatus status);

    // ===== UPDATE QUERIES =====

    // Update backlog item status
    @Modifying
    @Transactional
    @Query("UPDATE BacklogItem b SET b.status = :status WHERE b.itemID = :id")
    int updateBacklogItemStatus(@Param("id") Long id,
                                 @Param("status") BacklogItem.ItemStatus status);

    // Update priority of a backlog item
    @Modifying
    @Transactional
    @Query("UPDATE BacklogItem b SET b.priority = :priority WHERE b.itemID = :id")
    int updateBacklogItemPriority(@Param("id") Long id,
                                   @Param("priority") BacklogItem.Priority priority);

    // Assign milestone to backlog item
    @Modifying
    @Transactional
    @Query("UPDATE BacklogItem b SET b.milestoneID = :milestoneID WHERE b.itemID = :id")
    int assignMilestone(@Param("id") Long id,
                        @Param("milestoneID") Long milestoneID);

    // Bulk update status for all items of a product
    @Modifying
    @Transactional
    @Query("UPDATE BacklogItem b SET b.status = :newStatus " +
           "WHERE b.productID = :productID AND b.status = :oldStatus")
    int bulkUpdateStatus(@Param("productID") Long productID,
                          @Param("oldStatus") BacklogItem.ItemStatus oldStatus,
                          @Param("newStatus") BacklogItem.ItemStatus newStatus);

    // ===== DELETE QUERIES =====

    // Delete all cancelled items of a product
    @Modifying
    @Transactional
    @Query("DELETE FROM BacklogItem b WHERE b.productID = :productID " +
           "AND b.status = 'Cancelled'")
    int deleteCancelledItemsByProduct(@Param("productID") Long productID);
}
