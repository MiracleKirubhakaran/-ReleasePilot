package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.PromotionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRequestRepository extends JpaRepository<PromotionRequest, Long> {

    // ===== SELECT QUERIES =====

    // Find promotions by release ID
    List<PromotionRequest> findByReleaseID(Long releaseID);

    // Find promotions by status
    List<PromotionRequest> findByStatus(PromotionRequest.PromotionStatus status);

    // Find promotions by requested user
    List<PromotionRequest> findByRequestedByID(Long requestedByID);

    // Find promotions by approved user
    List<PromotionRequest> findByApprovedByID(Long approvedByID);

    // Find promotions by source environment
    List<PromotionRequest> findByFromEnvID(Long fromEnvID);

    // Find promotions by target environment
    List<PromotionRequest> findByToEnvID(Long toEnvID);

    // Find promotions by release and status
    List<PromotionRequest> findByReleaseIDAndStatus(Long releaseID,
                                                     PromotionRequest.PromotionStatus status);

    // Custom query: find pending promotions for a target environment
    @Query("SELECT p FROM PromotionRequest p WHERE p.toEnvID = :envID " +
           "AND p.status = 'Pending' ORDER BY p.scheduledDateTime ASC")
    List<PromotionRequest> findPendingPromotionsForEnv(@Param("envID") Long envID);

    // ===== UPDATE QUERIES =====

    // Update promotion status
    @Modifying
    @Transactional
    @Query("UPDATE PromotionRequest p SET p.status = :status WHERE p.promotionID = :id")
    int updatePromotionStatus(@Param("id") Long id,
                               @Param("status") PromotionRequest.PromotionStatus status);

    // Approve promotion by setting approver and status
    @Modifying
    @Transactional
    @Query("UPDATE PromotionRequest p SET p.status = 'Approved', p.approvedByID = :approverID " +
           "WHERE p.promotionID = :id")
    int approvePromotion(@Param("id") Long id, @Param("approverID") Long approverID);

    // Set actual datetime when promotion completes
    @Modifying
    @Transactional
    @Query("UPDATE PromotionRequest p SET p.actualDateTime = :actual, " +
           "p.status = 'Completed' WHERE p.promotionID = :id")
    int completePromotion(@Param("id") Long id,
                           @Param("actual") LocalDateTime actual);

    // Rollback a promotion
    @Modifying
    @Transactional
    @Query("UPDATE PromotionRequest p SET p.status = 'RolledBack' WHERE p.promotionID = :id")
    int rollbackPromotion(@Param("id") Long id);

    // ===== DELETE QUERIES =====

    // Delete all rejected promotions for a release
    @Modifying
    @Transactional
    @Query("DELETE FROM PromotionRequest p WHERE p.releaseID = :releaseID " +
           "AND p.status = 'Rejected'")
    int deleteRejectedByRelease(@Param("releaseID") Long releaseID);
}
