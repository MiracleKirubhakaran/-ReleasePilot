package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ===== SELECT QUERIES =====

    // Find notifications by user ID
    List<Notification> findByUserID(Long userID);

    // Find notifications by user and status
    List<Notification> findByUserIDAndStatus(Long userID, Notification.NotificationStatus status);

    // Find notifications by user and category
    List<Notification> findByUserIDAndCategory(Long userID,
                                                Notification.NotificationCategory category);

    // Count unread notifications for a user
    long countByUserIDAndStatus(Long userID, Notification.NotificationStatus status);

    // Find notifications by category
    List<Notification> findByCategory(Notification.NotificationCategory category);

    // Find notifications created after a date
    List<Notification> findByCreatedDateAfter(LocalDateTime date);

    // Find by user ordered by created date descending
    List<Notification> findByUserIDOrderByCreatedDateDesc(Long userID);

    // Custom query: get recent notifications for a user
    @Query("SELECT n FROM Notification n WHERE n.userID = :userID " +
           "ORDER BY n.createdDate DESC")
    List<Notification> findRecentByUser(@Param("userID") Long userID);

    // ===== UPDATE QUERIES =====

    // Mark a single notification as read
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = 'Read' WHERE n.notificationID = :id")
    int markAsRead(@Param("id") Long id);

    // Mark all unread notifications as read for a user
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = 'Read' WHERE n.userID = :userID " +
           "AND n.status = 'Unread'")
    int markAllAsReadByUser(@Param("userID") Long userID);

    // Dismiss a notification
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = 'Dismissed' WHERE n.notificationID = :id")
    int dismissNotification(@Param("id") Long id);

    // ===== DELETE QUERIES =====

    // Delete all read and dismissed notifications for a user
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.userID = :userID " +
           "AND n.status IN ('Read', 'Dismissed')")
    int deleteReadAndDismissedByUser(@Param("userID") Long userID);

    // Delete all notifications older than a given date for a user
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.userID = :userID " +
           "AND n.createdDate < :before")
    int deleteOldNotificationsByUser(@Param("userID") Long userID,
                                      @Param("before") LocalDateTime before);
}
