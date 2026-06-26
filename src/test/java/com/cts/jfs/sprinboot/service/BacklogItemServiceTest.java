package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.BacklogItem;
import com.cts.jfs.sprinboot.repository.BacklogItemRepository;
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
public class BacklogItemServiceTest {

    @Mock
    private BacklogItemRepository backlogItemRepository;

    @InjectMocks
    private BacklogItemService backlogItemService;

    private BacklogItem item;

    @BeforeEach
    void setUp() {
        item = new BacklogItem();
        item.setItemID(1L);
        item.setProductID(1L);
        item.setTitle("Implement Login Feature");
        item.setType(BacklogItem.ItemType.Feature);
        item.setPriority(BacklogItem.Priority.High);
        item.setStoryPoints(5);
        item.setRequestedByID(1L);
        item.setStatus(BacklogItem.ItemStatus.New);
    }

    // ===== GET ALL =====
    @Test
    void testGetAllBacklogItems_Success() {
        when(backlogItemRepository.findAll()).thenReturn(Arrays.asList(item));

        List<BacklogItem> result = backlogItemService.getAllBacklogItems();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Implement Login Feature", result.get(0).getTitle());
    }

    // ===== GET BY ID =====
    @Test
    void testGetBacklogItemById_Success() {
        when(backlogItemRepository.findById(1L)).thenReturn(Optional.of(item));

        BacklogItem result = backlogItemService.getBacklogItemById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getItemID());
    }

    @Test
    void testGetBacklogItemById_NotFound() {
        when(backlogItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> backlogItemService.getBacklogItemById(99L));
    }

    // ===== SAVE =====
    @Test
    void testSaveBacklogItem_DefaultStatusNew() {
        item.setStatus(null);
        when(backlogItemRepository.save(any(BacklogItem.class))).thenReturn(item);

        BacklogItem result = backlogItemService.saveBacklogItem(item);

        verify(backlogItemRepository, times(1)).save(any(BacklogItem.class));
    }

    @Test
    void testSaveBacklogItem_Success() {
        when(backlogItemRepository.save(any(BacklogItem.class))).thenReturn(item);

        BacklogItem result = backlogItemService.saveBacklogItem(item);

        assertNotNull(result);
        assertEquals("Implement Login Feature", result.getTitle());
    }

    // ===== UPDATE =====
    @Test
    void testUpdateBacklogItem_Success() {
        BacklogItem updated = new BacklogItem();
        updated.setTitle("Updated Feature");
        updated.setType(BacklogItem.ItemType.Enhancement);
        updated.setPriority(BacklogItem.Priority.Critical);
        updated.setStoryPoints(8);
        updated.setStatus(BacklogItem.ItemStatus.InDevelopment);

        when(backlogItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(backlogItemRepository.save(any(BacklogItem.class))).thenReturn(updated);

        BacklogItem result = backlogItemService.updateBacklogItem(1L, updated);

        assertNotNull(result);
        assertEquals("Updated Feature", result.getTitle());
    }

    // ===== DELETE =====
    @Test
    void testDeleteBacklogItem_Success() {
        doNothing().when(backlogItemRepository).deleteById(1L);

        backlogItemService.deleteBacklogItem(1L);

        verify(backlogItemRepository, times(1)).deleteById(1L);
    }

    // ===== GET BY PRODUCT =====
    @Test
    void testGetBacklogByProduct_Success() {
        when(backlogItemRepository.findByProductID(1L)).thenReturn(Arrays.asList(item));

        List<BacklogItem> result = backlogItemService.getBacklogByProduct(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // ===== GET BY PRIORITY =====
    @Test
    void testGetByPriority_Success() {
        when(backlogItemRepository.findByPriority(BacklogItem.Priority.High))
                .thenReturn(Arrays.asList(item));

        List<BacklogItem> result = backlogItemService.getByPriority(BacklogItem.Priority.High);

        assertNotNull(result);
        assertEquals(BacklogItem.Priority.High, result.get(0).getPriority());
    }

    // ===== GET BY STATUS =====
    @Test
    void testGetByStatus_Success() {
        when(backlogItemRepository.findByStatus(BacklogItem.ItemStatus.New))
                .thenReturn(Arrays.asList(item));

        List<BacklogItem> result = backlogItemService.getByStatus(BacklogItem.ItemStatus.New);

        assertNotNull(result);
        assertEquals(BacklogItem.ItemStatus.New, result.get(0).getStatus());
    }
}
