package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.ChangeFreezeWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChangeFreezeWindowRepository extends JpaRepository<ChangeFreezeWindow, Long> {

    // ===== SELECT QUERIES =====

    // Find freeze windows by product
    List<ChangeFreezeWindow> findByProductID(Long productID);

    // Find freeze windows by status
    List<ChangeFreezeWindow> findByStatus(ChangeFreezeWindow.FreezeStatus status);

    // Find freeze windows by product and status
    List<ChangeFreezeWindow> findByProductIDAndStatus(Long productID,
                                                       ChangeFreezeWindow.FreezeStatus status);

    // Find freeze windows approved by a user
    List<ChangeFreezeWindow> findByApprovedByID(Long approvedByID);

    // Custom query: find active freeze window covering a specific date
    @Query("SELECT f FROM ChangeFreezeWindow f WHERE f.productID = :productID " +
           "AND f.startDate <= :date AND f.endDate >= :date AND f.status = 'Active'")
    List<ChangeFreezeWindow> findActiveFreezeOnDate(@Param("productID") Long productID,
                                                     @Param("date") LocalDate date);

    // Custom query: find upcoming scheduled freeze windows
    @Query("SELECT f FROM ChangeFreezeWindow f WHERE f.productID = :productID " +
           "AND f.startDate > :today AND f.status = 'Scheduled'")
    List<ChangeFreezeWindow> findUpcomingFreezeWindows(@Param("productID") Long productID,
                                                        @Param("today") LocalDate today);

    // ===== UPDATE QUERIES =====

    // Lift a freeze window
    @Modifying
    @Transactional
    @Query("UPDATE ChangeFreezeWindow f SET f.status = 'Lifted' WHERE f.freezeID = :id")
    int liftFreezeWindow(@Param("id") Long id);

    // Activate a scheduled freeze window
    @Modifying
    @Transactional
    @Query("UPDATE ChangeFreezeWindow f SET f.status = 'Active' WHERE f.freezeID = :id")
    int activateFreezeWindow(@Param("id") Long id);

    // Extend freeze window end date
    @Modifying
    @Transactional
    @Query("UPDATE ChangeFreezeWindow f SET f.endDate = :newEndDate WHERE f.freezeID = :id")
    int extendFreezeWindow(@Param("id") Long id,
                            @Param("newEndDate") LocalDate newEndDate);

    // ===== DELETE QUERIES =====

    // Delete all lifted freeze windows for a product
    @Modifying
    @Transactional
    @Query("DELETE FROM ChangeFreezeWindow f WHERE f.productID = :productID " +
           "AND f.status = 'Lifted'")
    int deleteLiftedFreezeWindows(@Param("productID") Long productID);
}
