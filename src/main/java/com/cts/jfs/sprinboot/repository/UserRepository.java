package com.cts.jfs.sprinboot.repository;

import com.cts.jfs.sprinboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ===== SELECT QUERIES =====

    // Find by email
    Optional<User> findByEmail(String email);

    // Find by role
    List<User> findByRole(User.Role role);

    // Find by product ID
    List<User> findByProductID(Long productID);

    // Find by status
    List<User> findByStatus(User.UserStatus status);

    // Find by role and product
    List<User> findByRoleAndProductID(User.Role role, Long productID);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Custom query: find active users by product
    @Query("SELECT u FROM User u WHERE u.productID = :productID AND u.status = 'Active'")
    List<User> findActiveUsersByProduct(@Param("productID") Long productID);

    // ===== UPDATE QUERIES =====

    // Update user status by ID
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status WHERE u.userID = :id")
    int updateUserStatus(@Param("id") Long id,
                         @Param("status") User.UserStatus status);

    // Update user role by ID
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.role = :role WHERE u.userID = :id")
    int updateUserRole(@Param("id") Long id,
                       @Param("role") User.Role role);

    // Deactivate all users of a product
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = 'Inactive' WHERE u.productID = :productID")
    int deactivateUsersByProduct(@Param("productID") Long productID);

    // ===== DELETE QUERIES =====

    // Delete all inactive users of a product
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.productID = :productID AND u.status = 'Inactive'")
    int deleteInactiveUsersByProduct(@Param("productID") Long productID);
}
