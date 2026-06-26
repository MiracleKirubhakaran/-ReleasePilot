package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.ReleasePackage;
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
public interface ReleasePackageRepository extends JpaRepository<ReleasePackage, Long> {

    // ===== SELECT QUERIES =====

    // Find by product
    List<ReleasePackage> findByProductID(Long productID);

    // Find by status
    List<ReleasePackage> findByStatus(ReleasePackage.ReleaseStatus status);

    // Find by release type
    List<ReleasePackage> findByReleaseType(ReleasePackage.ReleaseType releaseType);

    // Find by product and status
    List<ReleasePackage> findByProductIDAndStatus(Long productID,
                                                   ReleasePackage.ReleaseStatus status);

    // Find by version number
    Optional<ReleasePackage> findByVersionNumber(String versionNumber);

    // Find releases due before a date
    List<ReleasePackage> findByTargetReleaseDateBefore(LocalDate date);

    // Find releases due after a date
    List<ReleasePackage> findByTargetReleaseDateAfter(LocalDate date);

    // Custom query: find latest released package for a product
    @Query("SELECT r FROM ReleasePackage r WHERE r.productID = :productID " +
           "AND r.status = 'Released' ORDER BY r.targetReleaseDate DESC")
    List<ReleasePackage> findLatestReleasedByProduct(@Param("productID") Long productID);

    // Custom query: count releases by product and type
    @Query("SELECT COUNT(r) FROM ReleasePackage r WHERE r.productID = :productID " +
           "AND r.releaseType = :type")
    Long countByProductAndType(@Param("productID") Long productID,
                                @Param("type") ReleasePackage.ReleaseType type);

    // ===== UPDATE QUERIES =====

    // Update release status
    @Modifying
    @Transactional
    @Query("UPDATE ReleasePackage r SET r.status = :status WHERE r.releaseID = :id")
    int updateReleaseStatus(@Param("id") Long id,
                             @Param("status") ReleasePackage.ReleaseStatus status);

    // Update release notes draft
    @Modifying
    @Transactional
    @Query("UPDATE ReleasePackage r SET r.releaseNotesDraft = :notes WHERE r.releaseID = :id")
    int updateReleaseNotesDraft(@Param("id") Long id,
                                 @Param("notes") String notes);

    // Update target release date
    @Modifying
    @Transactional
    @Query("UPDATE ReleasePackage r SET r.targetReleaseDate = :date WHERE r.releaseID = :id")
    int updateTargetReleaseDate(@Param("id") Long id,
                                 @Param("date") LocalDate date);

    // ===== DELETE QUERIES =====

    // Delete all recalled releases of a product
    @Modifying
    @Transactional
    @Query("DELETE FROM ReleasePackage r WHERE r.productID = :productID " +
           "AND r.status = 'Recalled'")
    int deleteRecalledReleases(@Param("productID") Long productID);
}
