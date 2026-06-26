package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.Sprint;
import com.cts.jfs.sprinboot.model.SprintItem;
import com.cts.jfs.sprinboot.repository.SprintRepository;
import com.cts.jfs.sprinboot.repository.SprintItemRepository;
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
public class SprintServiceTest {

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private SprintItemRepository sprintItemRepository;

    @InjectMocks
    private SprintService sprintService;

    private Sprint sprint;
    private SprintItem sprintItem;

    @BeforeEach
    void setUp() {
        sprint = new Sprint();
        sprint.setSprintID(1L);
        sprint.setProductID(1L);
        sprint.setSprintName("Sprint 1");
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusDays(14));
        sprint.setGoal("Complete Login Module");
        sprint.setTotalStoryPoints(20);
        sprint.setCompletedPoints(0);
        sprint.setStatus(Sprint.SprintStatus.Planned);

        sprintItem = new SprintItem();
        sprintItem.setSprintItemID(1L);
        sprintItem.setSprintID(1L);
        sprintItem.setBacklogItemID(1L);
        sprintItem.setAssignedDevID(2L);
        sprintItem.setEstimatedHours(8.0);
        sprintItem.setStatus(SprintItem.SprintItemStatus.ToDo);
    }

    // ===== GET ALL SPRINTS =====
    @Test
    void testGetAllSprints_Success() {
        when(sprintRepository.findAll()).thenReturn(Arrays.asList(sprint));

        List<Sprint> result = sprintService.getAllSprints();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sprint 1", result.get(0).getSprintName());
    }

    // ===== GET SPRINT BY ID =====
    @Test
    void testGetSprintById_Success() {
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));

        Sprint result = sprintService.getSprintById(1L);

        assertNotNull(result);
        assertEquals("Sprint 1", result.getSprintName());
    }

    @Test
    void testGetSprintById_NotFound() {
        when(sprintRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sprintService.getSprintById(99L));
    }

    // ===== SAVE SPRINT =====
    @Test
    void testSaveSprint_DefaultStatus() {
        sprint.setStatus(null);
        sprint.setCompletedPoints(null);
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        Sprint result = sprintService.saveSprint(sprint);

        verify(sprintRepository, times(1)).save(any(Sprint.class));
    }

    @Test
    void testSaveSprint_Success() {
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        Sprint result = sprintService.saveSprint(sprint);

        assertNotNull(result);
        assertEquals("Sprint 1", result.getSprintName());
    }

    // ===== UPDATE SPRINT =====
    @Test
    void testUpdateSprint_Success() {
        Sprint updated = new Sprint();
        updated.setSprintName("Sprint 1 Updated");
        updated.setGoal("Updated Goal");
        updated.setTotalStoryPoints(25);
        updated.setCompletedPoints(10);
        updated.setStatus(Sprint.SprintStatus.Active);
        updated.setStartDate(LocalDate.now());
        updated.setEndDate(LocalDate.now().plusDays(14));

        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(sprintRepository.save(any(Sprint.class))).thenReturn(updated);

        Sprint result = sprintService.updateSprint(1L, updated);

        assertNotNull(result);
        assertEquals("Sprint 1 Updated", result.getSprintName());
    }

    // ===== DELETE SPRINT =====
    @Test
    void testDeleteSprint_Success() {
        doNothing().when(sprintRepository).deleteById(1L);

        sprintService.deleteSprint(1L);

        verify(sprintRepository, times(1)).deleteById(1L);
    }

    // ===== GET BY PRODUCT =====
    @Test
    void testGetSprintsByProduct_Success() {
        when(sprintRepository.findByProductID(1L)).thenReturn(Arrays.asList(sprint));

        List<Sprint> result = sprintService.getSprintsByProduct(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // ===== ADD SPRINT ITEM =====
    @Test
    void testAddItemToSprint_Success() {
        when(sprintItemRepository.save(any(SprintItem.class))).thenReturn(sprintItem);

        SprintItem result = sprintService.addItemToSprint(sprintItem);

        assertNotNull(result);
        assertEquals(SprintItem.SprintItemStatus.ToDo, result.getStatus());
    }

    // ===== GET SPRINT ITEMS BY SPRINT ID =====
    @Test
    void testGetSprintItemsBySprintId_Success() {
        when(sprintItemRepository.findBySprintID(1L)).thenReturn(Arrays.asList(sprintItem));

        List<SprintItem> result = sprintService.getSprintItemsBySprintId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // ===== UPDATE SPRINT ITEM STATUS =====
    @Test
    void testUpdateSprintItemStatus_Success() {
        when(sprintItemRepository.findById(1L)).thenReturn(Optional.of(sprintItem));
        when(sprintItemRepository.save(any(SprintItem.class))).thenReturn(sprintItem);

        SprintItem result = sprintService.updateSprintItemStatus(
                1L, SprintItem.SprintItemStatus.InProgress);

        assertNotNull(result);
        verify(sprintItemRepository, times(1)).save(any(SprintItem.class));
    }
}
