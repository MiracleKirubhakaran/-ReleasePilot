package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.ReleaseGate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReleaseGateRepository extends JpaRepository<ReleaseGate, Long> {

    // ===== SELECT QUERIES =====

    // Find gates by release ID
    List<ReleaseGate> findByReleaseID(Long releaseID);

    // Find gates by gate type
    List<ReleaseGate> findByGateType(ReleaseGate.GateType gateType);

    // Find gates by outcome
    List<ReleaseGate> findByOutcome(ReleaseGate.GateOutcome outcome);

    // Find gates by status
    List<ReleaseGate> findByStatus(ReleaseGate.GateStatus status);

    // Find gates by release and status
    List<ReleaseGate> findByReleaseIDAndStatus(Long releaseID, ReleaseGate.GateStatus status);

    // Find gates by release and outcome
    List<ReleaseGate> findByReleaseIDAndOutcome(Long releaseID, ReleaseGate.GateOutcome outcome);

    // Custom query: count failed gates for a release
    @Query("SELECT COUNT(g) FROM ReleaseGate g WHERE g.releaseID = :releaseID " +
           "AND g.outcome = 'Fail'")
    Long countFailedGatesByRelease(@Param("releaseID") Long releaseID);

    // Custom query: count pending gates for a release
    @Query("SELECT COUNT(g) FROM ReleaseGate g WHERE g.releaseID = :releaseID " +
           "AND g.status = 'Pending'")
    Long countPendingGatesByRelease(@Param("releaseID") Long releaseID);

    // ===== UPDATE QUERIES =====

    // Evaluate a gate with outcome
    @Modifying
    @Transactional
    @Query("UPDATE ReleaseGate g SET g.outcome = :outcome, g.status = 'Evaluated', " +
           "g.evaluatedByID = :evaluatorID WHERE g.gateID = :id")
    int evaluateGate(@Param("id") Long id,
                      @Param("outcome") ReleaseGate.GateOutcome outcome,
                      @Param("evaluatorID") Long evaluatorID);

    // Update actual value of a gate
    @Modifying
    @Transactional
    @Query("UPDATE ReleaseGate g SET g.actualValue = :value WHERE g.gateID = :id")
    int updateActualValue(@Param("id") Long id, @Param("value") Double value);

    // Override a failed gate
    @Modifying
    @Transactional
    @Query("UPDATE ReleaseGate g SET g.outcome = 'Override', g.status = 'Evaluated' " +
           "WHERE g.gateID = :id")
    int overrideGate(@Param("id") Long id);

    // ===== DELETE QUERIES =====

    // Delete all gates for a release
    @Modifying
    @Transactional
    @Query("DELETE FROM ReleaseGate g WHERE g.releaseID = :releaseID")
    int deleteAllGatesByRelease(@Param("releaseID") Long releaseID);
}
