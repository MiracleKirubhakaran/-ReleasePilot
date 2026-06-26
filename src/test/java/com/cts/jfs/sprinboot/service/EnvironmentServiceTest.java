package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.Environment;
import com.cts.jfs.sprinboot.model.PromotionRequest;
import com.cts.jfs.sprinboot.model.ChangeFreezeWindow;
import com.cts.jfs.sprinboot.repository.EnvironmentRepository;
import com.cts.jfs.sprinboot.repository.PromotionRequestRepository;
import com.cts.jfs.sprinboot.repository.ChangeFreezeWindowRepository;
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
public class EnvironmentServiceTest {

    @Mock
    private EnvironmentRepository environmentRepository;

    @Mock
    private PromotionRequestRepository promotionRequestRepository;

    @Mock
    private ChangeFreezeWindowRepository changeFreezeWindowRepository;

    @InjectMocks
    private EnvironmentService environmentService;

    private Environment environment;
    private PromotionRequest promotion;
    private ChangeFreezeWindow freeze;

    @BeforeEach
    void setUp() {
        environment = new Environment();
        environment.setEnvID(1L);
        environment.setProductID(1L);
        environment.setEnvName(Environment.EnvName.Dev);
        environment.setOwnerID(1L);
        environment.setCurrentVersion("1.0.0");
        environment.setStatus(Environment.EnvStatus.Active);

        promotion = new PromotionRequest();
        promotion.setPromotionID(1L);
        promotion.setReleaseID(1L);
        promotion.setFromEnvID(1L);
        promotion.setToEnvID(2L);
        promotion.setRequestedByID(1L);
        promotion.setStatus(PromotionRequest.PromotionStatus.Pending);

        freeze = new ChangeFreezeWindow();
        freeze.setFreezeID(1L);
        freeze.setProductID(1L);
        freeze.setStartDate(LocalDate.now());
        freeze.setEndDate(LocalDate.now().plusDays(7));
        freeze.setReason("Year-end freeze");
        freeze.setStatus(ChangeFreezeWindow.FreezeStatus.Scheduled);
    }

    // ===== ENVIRONMENT TESTS =====

    @Test
    void testGetAllEnvironments_Success() {
        when(environmentRepository.findAll()).thenReturn(Arrays.asList(environment));

        List<Environment> result = environmentService.getAllEnvironments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Environment.EnvName.Dev, result.get(0).getEnvName());
    }

    @Test
    void testGetEnvironmentById_Success() {
        when(environmentRepository.findById(1L)).thenReturn(Optional.of(environment));

        Environment result = environmentService.getEnvironmentById(1L);

        assertNotNull(result);
        assertEquals(Environment.EnvName.Dev, result.getEnvName());
    }

    @Test
    void testSaveEnvironment_DefaultActive() {
        environment.setStatus(null);
        when(environmentRepository.save(any(Environment.class))).thenReturn(environment);

        environmentService.saveEnvironment(environment);

        verify(environmentRepository, times(1)).save(any(Environment.class));
    }

    @Test
    void testDeleteEnvironment_Success() {
        doNothing().when(environmentRepository).deleteById(1L);

        environmentService.deleteEnvironment(1L);

        verify(environmentRepository, times(1)).deleteById(1L);
    }

    // ===== PROMOTION TESTS =====

    @Test
    void testGetAllPromotions_Success() {
        when(promotionRequestRepository.findAll()).thenReturn(Arrays.asList(promotion));

        List<PromotionRequest> result = environmentService.getAllPromotions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(PromotionRequest.PromotionStatus.Pending, result.get(0).getStatus());
    }

    @Test
    void testSavePromotion_DefaultPending() {
        when(promotionRequestRepository.save(any(PromotionRequest.class))).thenReturn(promotion);

        PromotionRequest result = environmentService.savePromotion(promotion);

        assertNotNull(result);
        assertEquals(PromotionRequest.PromotionStatus.Pending, result.getStatus());
    }

    @Test
    void testApprovePromotion_Success() {
        when(promotionRequestRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(promotionRequestRepository.save(any(PromotionRequest.class))).thenReturn(promotion);

        PromotionRequest result = environmentService.approvePromotion(1L, 2L);

        assertNotNull(result);
        verify(promotionRequestRepository, times(1)).save(any(PromotionRequest.class));
    }

    @Test
    void testRejectPromotion_Success() {
        when(promotionRequestRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(promotionRequestRepository.save(any(PromotionRequest.class))).thenReturn(promotion);

        PromotionRequest result = environmentService.rejectPromotion(1L);

        assertNotNull(result);
        verify(promotionRequestRepository, times(1)).save(any(PromotionRequest.class));
    }

    // ===== FREEZE WINDOW TESTS =====

    @Test
    void testGetAllFreezeWindows_Success() {
        when(changeFreezeWindowRepository.findAll()).thenReturn(Arrays.asList(freeze));

        List<ChangeFreezeWindow> result = environmentService.getAllFreezeWindows();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Year-end freeze", result.get(0).getReason());
    }

    @Test
    void testSaveFreezeWindow_DefaultScheduled() {
        freeze.setStatus(null);
        when(changeFreezeWindowRepository.save(any(ChangeFreezeWindow.class))).thenReturn(freeze);

        environmentService.saveFreezeWindow(freeze);

        verify(changeFreezeWindowRepository, times(1)).save(any(ChangeFreezeWindow.class));
    }

    @Test
    void testLiftFreezeWindow_Success() {
        when(changeFreezeWindowRepository.findById(1L)).thenReturn(Optional.of(freeze));
        when(changeFreezeWindowRepository.save(any(ChangeFreezeWindow.class))).thenReturn(freeze);

        ChangeFreezeWindow result = environmentService.liftFreezeWindow(1L);

        assertNotNull(result);
        verify(changeFreezeWindowRepository, times(1)).save(any(ChangeFreezeWindow.class));
    }

    @Test
    void testIsProductFrozen_False() {
        when(changeFreezeWindowRepository.findByProductIDAndStatus(
                1L, ChangeFreezeWindow.FreezeStatus.Active))
                .thenReturn(Arrays.asList());

        boolean result = environmentService.isProductFrozen(1L);

        assertFalse(result);
    }
}
