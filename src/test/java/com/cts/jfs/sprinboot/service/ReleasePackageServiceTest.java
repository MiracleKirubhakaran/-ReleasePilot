package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.ReleasePackage;
import com.cts.jfs.sprinboot.model.PatchDependency;
import com.cts.jfs.sprinboot.repository.ReleasePackageRepository;
import com.cts.jfs.sprinboot.repository.PatchDependencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReleasePackageServiceTest {

    @Mock
    private ReleasePackageRepository releasePackageRepository;

    @Mock
    private PatchDependencyRepository patchDependencyRepository;

    @InjectMocks
    private ReleasePackageService releasePackageService;

    private ReleasePackage release;

    @BeforeEach
    void setUp() {
        release = new ReleasePackage();
        release.setReleaseID(1L);
        release.setProductID(1L);
        release.setVersionNumber("1.0.0");
        release.setReleaseType(ReleasePackage.ReleaseType.Minor);
        release.setIncludedItemIDs("1,2,3");
        release.setTargetReleaseDate(LocalDate.now().plusDays(30));
        release.setReleasedByID(1L);
        release.setStatus(ReleasePackage.ReleaseStatus.Draft);
    }

    // ===== GET ALL =====
    @Test
    void testGetAllReleases_Success() {
        when(releasePackageRepository.findAll()).thenReturn(Arrays.asList(release));

        List<ReleasePackage> result = releasePackageService.getAllReleases();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1.0.0", result.get(0).getVersionNumber());
    }

    // ===== GET BY ID =====
    @Test
    void testGetReleaseById_Success() {
        when(releasePackageRepository.findById(1L)).thenReturn(Optional.of(release));

        ReleasePackage result = releasePackageService.getReleaseById(1L);

        assertNotNull(result);
        assertEquals("1.0.0", result.getVersionNumber());
    }

    @Test
    void testGetReleaseById_NotFound() {
        when(releasePackageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> releasePackageService.getReleaseById(99L));
    }

    // ===== SAVE =====
    @Test
    void testSaveRelease_DefaultStatusDraft() {
        release.setStatus(null);
        when(releasePackageRepository.save(any(ReleasePackage.class))).thenReturn(release);

        releasePackageService.saveRelease(release);

        verify(releasePackageRepository, times(1)).save(any(ReleasePackage.class));
    }

    @Test
    void testSaveRelease_Success() {
        when(releasePackageRepository.save(any(ReleasePackage.class))).thenReturn(release);

        ReleasePackage result = releasePackageService.saveRelease(release);

        assertNotNull(result);
        assertEquals("1.0.0", result.getVersionNumber());
    }

    // ===== UPDATE =====
    @Test
    void testUpdateRelease_Success() {
        ReleasePackage updated = new ReleasePackage();
        updated.setVersionNumber("1.1.0");
        updated.setReleaseType(ReleasePackage.ReleaseType.Patch);
        updated.setStatus(ReleasePackage.ReleaseStatus.PackagingComplete);
        updated.setIncludedItemIDs("1,2,3,4");
        updated.setTargetReleaseDate(LocalDate.now().plusDays(15));
        updated.setReleasedByID(1L);

        when(releasePackageRepository.findById(1L)).thenReturn(Optional.of(release));
        when(releasePackageRepository.save(any(ReleasePackage.class))).thenReturn(updated);

        ReleasePackage result = releasePackageService.updateRelease(1L, updated);

        assertNotNull(result);
        assertEquals("1.1.0", result.getVersionNumber());
    }

    // ===== DELETE =====
    @Test
    void testDeleteRelease_Success() {
        doNothing().when(releasePackageRepository).deleteById(1L);

        releasePackageService.deleteRelease(1L);

        verify(releasePackageRepository, times(1)).deleteById(1L);
    }

    // ===== GET BY STATUS =====
    @Test
    void testGetReleasesByStatus_Success() {
        when(releasePackageRepository.findByStatus(ReleasePackage.ReleaseStatus.Draft))
                .thenReturn(Arrays.asList(release));

        List<ReleasePackage> result = releasePackageService.getReleasesByStatus(
                ReleasePackage.ReleaseStatus.Draft);

        assertNotNull(result);
        assertEquals(ReleasePackage.ReleaseStatus.Draft, result.get(0).getStatus());
    }

    // ===== RECALL =====
    @Test
    void testRecallRelease_Success() {
        when(releasePackageRepository.findById(1L)).thenReturn(Optional.of(release));
        when(releasePackageRepository.save(any(ReleasePackage.class))).thenReturn(release);

        ReleasePackage result = releasePackageService.recallRelease(1L);

        assertNotNull(result);
        verify(releasePackageRepository, times(1)).save(any(ReleasePackage.class));
    }
}
