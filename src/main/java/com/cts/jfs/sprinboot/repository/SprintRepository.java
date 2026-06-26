package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

    // ===== SELECT QUERIES =====

    // Find sprints by product
    List<Sprint> findByProductID(Long productID);

    // Find sprints by status
    List<Sprint> findByStatus(Sprint.SprintStatus status);

    // Find sprints by product and status
    List<Sprint> findByProductIDAndStatus(Long productID, Sprint.SprintStatus status);

    // Find sprints starting after a date
    List<Sprint> findByStartDateAfter(LocalDate date);

    // Find sprints ending before a date
    List<Sprint> findByEndDateBefore(LocalDate date);

    // Custom query: find overlapping sprints for a product
    @Query("SELECT s FROM Sprint s WHERE s.productID = :productID " +
           "AND s.startDate <= :endDate AND s.endDate >= :startDate")
    List<Sprint> findOverlappingSprints(@Param("productID") Long productID,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    // Custom query: total velocity for a product
    @Query("SELECT SUM(s.completedPoints) FROM Sprint s WHERE s.productID = :productID " +
           "AND s.status = 'Completed'")
    Integer getTotalVelocityByProduct(@Param("productID") Long productID);

    // ===== UPDATE QUERIES =====

    // Update sprint status
    @Modifying
    @Transactional
    @Query("UPDATE Sprint s SET s.status = :status WHERE s.sprintID = :id")
    int updateSprintStatus(@Param("id") Long id,
                            @Param("status") Sprint.SprintStatus status);

    // Update completed points
    @Modifying
    @Transactional
    @Query("UPDATE Sprint s SET s.completedPoints = :points WHERE s.sprintID = :id")
    int updateCompletedPoints(@Param("id") Long id,
                               @Param("points") Integer points);

    // ===== DELETE QUERIES =====

    // Delete all cancelled sprints for a product
    @Modifying
    @Transactional
    @Query("DELETE FROM Sprint s WHERE s.productID = :productID AND s.status = 'Cancelled'")
    int deleteCancelledSprints(@Param("productID") Long productID);
}
