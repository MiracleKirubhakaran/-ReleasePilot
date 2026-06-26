package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, Long> {

    // ===== SELECT QUERIES =====

    // Find environments by product
    List<Environment> findByProductID(Long productID);

    // Find environments by status
    List<Environment> findByStatus(Environment.EnvStatus status);

    // Find environment by product and env name
    Optional<Environment> findByProductIDAndEnvName(Long productID, Environment.EnvName envName);

    // Find environments by owner
    List<Environment> findByOwnerID(Long ownerID);

    // Find environments by product and status
    List<Environment> findByProductIDAndStatus(Long productID, Environment.EnvStatus status);

    // Custom query: find all active environments for a product
    @Query("SELECT e FROM Environment e WHERE e.productID = :productID AND e.status = 'Active'")
    List<Environment> findActiveEnvironments(@Param("productID") Long productID);

    // ===== UPDATE QUERIES =====

    // Update environment status
    @Modifying
    @Transactional
    @Query("UPDATE Environment e SET e.status = :status WHERE e.envID = :id")
    int updateEnvironmentStatus(@Param("id") Long id,
                                 @Param("status") Environment.EnvStatus status);

    // Update current version of environment after promotion
    @Modifying
    @Transactional
    @Query("UPDATE Environment e SET e.currentVersion = :version, " +
           "e.lastPromotionDate = :date WHERE e.envID = :id")
    int updateVersionAfterPromotion(@Param("id") Long id,
                                     @Param("version") String version,
                                     @Param("date") LocalDate date);

    // Freeze an environment
    @Modifying
    @Transactional
    @Query("UPDATE Environment e SET e.status = 'Frozen' WHERE e.envID = :id")
    int freezeEnvironment(@Param("id") Long id);

    // Unfreeze an environment
    @Modifying
    @Transactional
    @Query("UPDATE Environment e SET e.status = 'Active' WHERE e.envID = :id")
    int unfreezeEnvironment(@Param("id") Long id);

    // ===== DELETE QUERIES =====

    // Delete all environments for a product
    @Modifying
    @Transactional
    @Query("DELETE FROM Environment e WHERE e.productID = :productID")
    int deleteAllByProduct(@Param("productID") Long productID);
}
