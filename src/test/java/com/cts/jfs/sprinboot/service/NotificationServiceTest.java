package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.Notification;
import com.cts.jfs.sprinboot.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setNotificationID(1L);
        notification.setUserID(1L);
        notification.setMessage("Sprint 1 has been completed");
        notification.setCategory(Notification.NotificationCategory.Sprint);
        notification.setStatus(Notification.NotificationStatus.Unread);
        notification.setCreatedDate(LocalDateTime.now());
    }

    // ===== GET BY USER =====
    @Test
    void testGetNotificationsByUser_Success() {
        when(notificationRepository.findByUserID(1L)).thenReturn(Arrays.asList(notification));

        List<Notification> result = notificationService.getNotificationsByUser(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sprint 1 has been completed", result.get(0).getMessage());
    }

    // ===== GET UNREAD =====
    @Test
    void testGetUnreadNotifications_Success() {
        when(notificationRepository.findByUserIDAndStatus(
                1L, Notification.NotificationStatus.Unread))
                .thenReturn(Arrays.asList(notification));

        List<Notification> result = notificationService.getUnreadNotifications(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Notification.NotificationStatus.Unread, result.get(0).getStatus());
    }

    // ===== GET UNREAD COUNT =====
    @Test
    void testGetUnreadCount_Success() {
        when(notificationRepository.countByUserIDAndStatus(
                1L, Notification.NotificationStatus.Unread)).thenReturn(5L);

        long count = notificationService.getUnreadCount(1L);

        assertEquals(5L, count);
    }

    // ===== SEND NOTIFICATION =====
    @Test
    void testSendNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.sendNotification(
                1L, "Test message", Notification.NotificationCategory.Release);

        assertNotNull(result);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    // ===== MARK AS READ =====
    @Test
    void testMarkAsRead_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.markAsRead(1L);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testMarkAsRead_NotFound() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(99L));
    }

    // ===== MARK ALL AS READ =====
    @Test
    void testMarkAllAsRead_Success() {
        when(notificationRepository.findByUserIDAndStatus(
                1L, Notification.NotificationStatus.Unread))
                .thenReturn(Arrays.asList(notification));
        when(notificationRepository.saveAll(anyList())).thenReturn(Arrays.asList(notification));

        notificationService.markAllAsRead(1L);

        verify(notificationRepository, times(1)).saveAll(anyList());
    }

    // ===== DISMISS =====
    @Test
    void testDismissNotification_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.dismissNotification(1L);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    // ===== DELETE =====
    @Test
    void testDeleteNotification_Success() {
        doNothing().when(notificationRepository).deleteById(1L);

        notificationService.deleteNotification(1L);

        verify(notificationRepository, times(1)).deleteById(1L);
    }

    // ===== GET BY CATEGORY =====
    @Test
    void testGetByCategory_Success() {
        when(notificationRepository.findByUserIDAndCategory(
                1L, Notification.NotificationCategory.Sprint))
                .thenReturn(Arrays.asList(notification));

        List<Notification> result = notificationService.getByCategory(
                1L, Notification.NotificationCategory.Sprint);

        assertNotNull(result);
        assertEquals(Notification.NotificationCategory.Sprint, result.get(0).getCategory());
    }
}
