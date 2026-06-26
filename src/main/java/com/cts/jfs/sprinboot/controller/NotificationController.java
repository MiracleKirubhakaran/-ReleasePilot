package com.cts.jfs.sprinboot.controller;

import com.cts.jfs.sprinboot.model.Notification;
import com.cts.jfs.sprinboot.service.NotificationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Get all notifications for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    // Get unread notifications
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    // Get unread count
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    // Mark as read
    @PostMapping("/read/{id}")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Marked as read");
    }

    // Mark all as read
    @PostMapping("/readAll/{userId}")
    public ResponseEntity<String> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("All marked as read");
    }

    // Dismiss notification
    @PostMapping("/dismiss/{id}")
    public ResponseEntity<String> dismissNotification(@PathVariable Long id) {
        notificationService.dismissNotification(id);
        return ResponseEntity.ok("Notification dismissed");
    }

    // Delete notification
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification deleted");
    }

    // Get by category
    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<List<Notification>> getByCategory(
            @PathVariable Long userId,
            @PathVariable String category) {
        return ResponseEntity.ok(notificationService.getByCategory(
                userId, Notification.NotificationCategory.valueOf(category)));
    }
}
