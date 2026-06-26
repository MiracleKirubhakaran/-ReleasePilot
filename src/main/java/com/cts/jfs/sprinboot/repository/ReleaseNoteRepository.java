package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.ReleaseNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReleaseNoteRepository extends JpaRepository<ReleaseNote, Long> {

    // ===== SELECT QUERIES =====

    // Find notes by release ID
    List<ReleaseNote> findByReleaseID(Long releaseID);

    // Find notes by status
    List<ReleaseNote> findByStatus(ReleaseNote.NoteStatus status);

    // Find notes by version number
    Optional<ReleaseNote> findByVersionNumber(String versionNumber);

    // Find notes by authored user
    List<ReleaseNote> findByAuthoredByID(Long authoredByID);

    // Find notes by release and status
    List<ReleaseNote> findByReleaseIDAndStatus(Long releaseID, ReleaseNote.NoteStatus status);

    // Check if note exists for release
    boolean existsByReleaseID(Long releaseID);

    // Custom query: find all published notes ordered by version
    @Query("SELECT r FROM ReleaseNote r WHERE r.status = 'Published' " +
           "ORDER BY r.versionNumber DESC")
    List<ReleaseNote> findAllPublishedNotes();

    // ===== UPDATE QUERIES =====

    // Update release note status
    @Modifying
    @Transactional
    @Query("UPDATE ReleaseNote r SET r.status = :status WHERE r.noteID = :id")
    int updateNoteStatus(@Param("id") Long id,
                          @Param("status") ReleaseNote.NoteStatus status);

    // Approve a release note
    @Modifying
    @Transactional
    @Query("UPDATE ReleaseNote r SET r.status = 'Approved' WHERE r.noteID = :id " +
           "AND r.status = 'Review'")
    int approveNote(@Param("id") Long id);

    // Publish a release note
    @Modifying
    @Transactional
    @Query("UPDATE ReleaseNote r SET r.status = 'Published' WHERE r.noteID = :id " +
           "AND r.status = 'Approved'")
    int publishNote(@Param("id") Long id);

    // ===== DELETE QUERIES =====

    // Delete draft notes for a release
    @Modifying
    @Transactional
    @Query("DELETE FROM ReleaseNote r WHERE r.releaseID = :releaseID AND r.status = 'Draft'")
    int deleteDraftNotesByRelease(@Param("releaseID") Long releaseID);
}
