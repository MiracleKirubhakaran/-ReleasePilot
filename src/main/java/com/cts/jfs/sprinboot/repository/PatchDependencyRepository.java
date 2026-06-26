package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.PatchDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PatchDependencyRepository extends JpaRepository<PatchDependency, Long> {

    // ===== SELECT QUERIES =====

    // Find dependencies by release ID
    List<PatchDependency> findByReleaseID(Long releaseID);

    // Find by the release it depends on
    List<PatchDependency> findByDependsOnReleaseID(Long dependsOnReleaseID);

    // Find by dependency type
    List<PatchDependency> findByDependencyType(PatchDependency.DependencyType dependencyType);

    // Find by release and dependency type
    List<PatchDependency> findByReleaseIDAndDependencyType(
            Long releaseID, PatchDependency.DependencyType dependencyType);

    // Custom query: find all mandatory prerequisites for a release
    @Query("SELECT p FROM PatchDependency p WHERE p.releaseID = :releaseID " +
           "AND p.dependencyType = 'MustInstallBefore'")
    List<PatchDependency> findMandatoryPrerequisites(@Param("releaseID") Long releaseID);

    // ===== UPDATE QUERIES =====

    // Update dependency type
    @Modifying
    @Transactional
    @Query("UPDATE PatchDependency p SET p.dependencyType = :type WHERE p.dependencyID = :id")
    int updateDependencyType(@Param("id") Long id,
                              @Param("type") PatchDependency.DependencyType type);

    // Update notes for a dependency
    @Modifying
    @Transactional
    @Query("UPDATE PatchDependency p SET p.notes = :notes WHERE p.dependencyID = :id")
    int updateNotes(@Param("id") Long id, @Param("notes") String notes);

    // ===== DELETE QUERIES =====

    // Delete all dependencies of a release
    @Modifying
    @Transactional
    @Query("DELETE FROM PatchDependency p WHERE p.releaseID = :releaseID")
    int deleteAllByRelease(@Param("releaseID") Long releaseID);
}
