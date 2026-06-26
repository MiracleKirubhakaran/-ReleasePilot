package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.User;
import com.cts.jfs.sprinboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.cts.jfs.sprinboot.exception.ResourceNotFoundException;
import com.cts.jfs.sprinboot.exception.DuplicateRecordException;
import com.cts.jfs.sprinboot.exception.InvalidOperationException;
import com.cts.jfs.sprinboot.exception.UnauthorizedException;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // Save new user
    public User saveUser(User user) {
        user.setStatus(User.UserStatus.Active);
        return userRepository.save(user);
    }

    // Update existing user
    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        existing.setPhone(updatedUser.getPhone());
        existing.setRole(updatedUser.getRole());
        existing.setProductID(updatedUser.getProductID());
        existing.setStatus(updatedUser.getStatus());
        return userRepository.save(existing);
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Get users by role
    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    // Get users by product
    public List<User> getUsersByProduct(Long productId) {
        return userRepository.findByProductID(productId);
    }

    // Get users by status
    public List<User> getUsersByStatus(User.UserStatus status) {
        return userRepository.findByStatus(status);
    }

    // Get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
