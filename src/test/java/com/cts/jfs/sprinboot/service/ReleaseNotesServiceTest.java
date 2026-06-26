package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.ReleaseNote;
import com.cts.jfs.sprinboot.model.CustomerAdvisory;
import com.cts.jfs.sprinboot.repository.ReleaseNoteRepository;
import com.cts.jfs.sprinboot.repository.CustomerAdvisoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReleaseNotesServiceTest {

    @Mock
    private ReleaseNoteRepository releaseNoteRepository;

    @Mock
    private CustomerAdvisoryRepository customerAdvisoryRepository;

    @InjectMocks
    private ReleaseNotesService releaseNotesService;

    private ReleaseNote note;
    private CustomerAdvisory advisory;

    @BeforeEach
    void setUp() {
        note = new ReleaseNote();
        note.setNoteID(1L);
        note.setReleaseID(1L);
        note.setVersionNumber("1.0.0");
        note.setSummary("First release of ReleasePilot");
        note.setNewFeatures("Login, Dashboard, Products");
        note.setBugFixes("None");
        note.setStatus(ReleaseNote.NoteStatus.Draft);
        note.setAuthoredByID(1L);

        advisory = new CustomerAdvisory();
        advisory.setAdvisoryID(1L);
        advisory.setReleaseID(1L);
        advisory.setTitle("Security Update Advisory");
        advisory.setSeverity(CustomerAdvisory.Severity.Critical);
        advisory.setAffectedVersions("1.0.0");
        advisory.setResolution("Upgrade to 1.0.1");
        advisory.setStatus(CustomerAdvisory.AdvisoryStatus.Draft);
    }

    // ===== RELEASE NOTE TESTS =====

    @Test
    void testGetAllReleaseNotes_Success() {
        when(releaseNoteRepository.findAll()).thenReturn(Arrays.asList(note));

        List<ReleaseNote> result = releaseNotesService.getAllReleaseNotes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1.0.0", result.get(0).getVersionNumber());
    }

    @Test
    void testGetReleaseNoteById_Success() {
        when(releaseNoteRepository.findById(1L)).thenReturn(Optional.of(note));

        ReleaseNote result = releaseNotesService.getReleaseNoteById(1L);

        assertNotNull(result);
        assertEquals("1.0.0", result.getVersionNumber());
    }

    @Test
    void testGetReleaseNoteById_NotFound() {
        when(releaseNoteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> releaseNotesService.getReleaseNoteById(99L));
    }

    @Test
    void testSaveReleaseNote_DefaultDraftStatus() {
        note.setStatus(null);
        when(releaseNoteRepository.save(any(ReleaseNote.class))).thenReturn(note);

        releaseNotesService.saveReleaseNote(note);

        verify(releaseNoteRepository, times(1)).save(any(ReleaseNote.class));
    }

    @Test
    void testPublishReleaseNote_Success() {
        note.setStatus(ReleaseNote.NoteStatus.Approved);
        when(releaseNoteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(releaseNoteRepository.save(any(ReleaseNote.class))).thenReturn(note);

        ReleaseNote result = releaseNotesService.publishReleaseNote(1L);

        assertNotNull(result);
        verify(releaseNoteRepository, times(1)).save(any(ReleaseNote.class));
    }

    @Test
    void testPublishReleaseNote_NotApproved_ThrowsException() {
        note.setStatus(ReleaseNote.NoteStatus.Draft);
        when(releaseNoteRepository.findById(1L)).thenReturn(Optional.of(note));

        assertThrows(RuntimeException.class,
                () -> releaseNotesService.publishReleaseNote(1L));
    }

    @Test
    void testApproveReleaseNote_Success() {
        when(releaseNoteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(releaseNoteRepository.save(any(ReleaseNote.class))).thenReturn(note);

        ReleaseNote result = releaseNotesService.approveReleaseNote(1L);

        assertNotNull(result);
        verify(releaseNoteRepository, times(1)).save(any(ReleaseNote.class));
    }

    @Test
    void testDeleteReleaseNote_Success() {
        doNothing().when(releaseNoteRepository).deleteById(1L);

        releaseNotesService.deleteReleaseNote(1L);

        verify(releaseNoteRepository, times(1)).deleteById(1L);
    }

    // ===== CUSTOMER ADVISORY TESTS =====

    @Test
    void testGetAllAdvisories_Success() {
        when(customerAdvisoryRepository.findAll()).thenReturn(Arrays.asList(advisory));

        List<CustomerAdvisory> result = releaseNotesService.getAllAdvisories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Security Update Advisory", result.get(0).getTitle());
    }

    @Test
    void testSaveAdvisory_DefaultDraftStatus() {
        advisory.setStatus(null);
        when(customerAdvisoryRepository.save(any(CustomerAdvisory.class))).thenReturn(advisory);

        releaseNotesService.saveAdvisory(advisory);

        verify(customerAdvisoryRepository, times(1)).save(any(CustomerAdvisory.class));
    }

    @Test
    void testPublishAdvisory_Success() {
        when(customerAdvisoryRepository.findById(1L)).thenReturn(Optional.of(advisory));
        when(customerAdvisoryRepository.save(any(CustomerAdvisory.class))).thenReturn(advisory);

        CustomerAdvisory result = releaseNotesService.publishAdvisory(1L);

        assertNotNull(result);
        verify(customerAdvisoryRepository, times(1)).save(any(CustomerAdvisory.class));
    }

    @Test
    void testDeleteAdvisory_Success() {
        doNothing().when(customerAdvisoryRepository).deleteById(1L);

        releaseNotesService.deleteAdvisory(1L);

        verify(customerAdvisoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAdvisoriesBySeverity_Success() {
        when(customerAdvisoryRepository.findBySeverity(CustomerAdvisory.Severity.Critical))
                .thenReturn(Arrays.asList(advisory));

        List<CustomerAdvisory> result = releaseNotesService.getAdvisoriesBySeverity(
                CustomerAdvisory.Severity.Critical);

        assertNotNull(result);
        assertEquals(CustomerAdvisory.Severity.Critical, result.get(0).getSeverity());
    }
}
