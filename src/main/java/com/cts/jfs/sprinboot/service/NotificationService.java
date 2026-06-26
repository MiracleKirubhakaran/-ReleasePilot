package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.Notification;
import com.cts.jfs.sprinboot.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.cts.jfs.sprinboot.exception.ResourceNotFoundException;
import com.cts.jfs.sprinboot.exception.DuplicateRecordException;
import com.cts.jfs.sprinboot.exception.InvalidOperationException;
import com.cts.jfs.sprinboot.exception.UnauthorizedException;
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Get all notifications for a user
    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserID(userId);
    }

    // Get unread notifications for a user
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIDAndStatus(userId, Notification.NotificationStatus.Unread);
    }

    // Get unread count for a user
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIDAndStatus(userId, Notification.NotificationStatus.Unread);
    }

    // Send a notification (called internally from other services)
    public Notification sendNotification(Long userId, String message,
                                          Notification.NotificationCategory category) {
        Notification notification = new Notification();
        notification.setUserID(userId);
        notification.setMessage(message);
        notification.setCategory(category);
        notification.setStatus(Notification.NotificationStatus.Unread);
        notification.setCreatedDate(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    // Mark notification as read
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));
        notification.setStatus(Notification.NotificationStatus.Read);
        notificationRepository.save(notification);
    }

    // Mark all notifications as read for a user
    public void markAllAsRead(Long userId) {
        List<Notification> unread = getUnreadNotifications(userId);
        unread.forEach(n -> n.setStatus(Notification.NotificationStatus.Read));
        notificationRepository.saveAll(unread);
    }

    // Dismiss a notification
    public void dismissNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));
        notification.setStatus(Notification.NotificationStatus.Dismissed);
        notificationRepository.save(notification);
    }

    // Delete a notification
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    // Get notifications by category for a user
    public List<Notification> getByCategory(Long userId, Notification.NotificationCategory category) {
        return notificationRepository.findByUserIDAndCategory(userId, category);
    }

    // Delete all dismissed notifications for a user
    public void clearDismissed(Long userId) {
        List<Notification> dismissed = notificationRepository
                .findByUserIDAndStatus(userId, Notification.NotificationStatus.Dismissed);
        notificationRepository.deleteAll(dismissed);
    }
}
