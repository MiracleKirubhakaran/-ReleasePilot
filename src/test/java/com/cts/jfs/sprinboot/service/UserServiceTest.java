package com.cts.jfs.sprinboot.service;

import com.cts.jfs.sprinboot.model.User;
import com.cts.jfs.sprinboot.repository.UserRepository;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserID(1L);
        user.setName("John Doe");
        user.setEmail("john@releasepilot.com");
        user.setPhone("9876543210");
        user.setRole(User.Role.ProductManager);
        user.setProductID(1L);
        user.setStatus(User.UserStatus.Active);
    }

    // ===== GET ALL USERS =====
    @Test
    void testGetAllUsers_Success() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    // ===== GET USER BY ID =====
    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserID());
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(99L));
    }

    // ===== SAVE USER =====
    @Test
    void testSaveUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals(User.UserStatus.Active, result.getStatus());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ===== UPDATE USER =====
    @Test
    void testUpdateUser_Success() {
        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane@releasepilot.com");
        updatedUser.setPhone("1234567890");
        updatedUser.setRole(User.Role.DevLead);
        updatedUser.setProductID(2L);
        updatedUser.setStatus(User.UserStatus.Active);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ===== DELETE USER =====
    @Test
    void testDeleteUser_Success() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    // ===== GET USERS BY ROLE =====
    @Test
    void testGetUsersByRole_Success() {
        when(userRepository.findByRole(User.Role.ProductManager))
                .thenReturn(Arrays.asList(user));

        List<User> result = userService.getUsersByRole(User.Role.ProductManager);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(User.Role.ProductManager, result.get(0).getRole());
    }

    // ===== GET USERS BY STATUS =====
    @Test
    void testGetUsersByStatus_Success() {
        when(userRepository.findByStatus(User.UserStatus.Active))
                .thenReturn(Arrays.asList(user));

        List<User> result = userService.getUsersByStatus(User.UserStatus.Active);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(User.UserStatus.Active, result.get(0).getStatus());
    }

    // ===== GET USER BY EMAIL =====
    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("john@releasepilot.com"))
                .thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("john@releasepilot.com");

        assertNotNull(result);
        assertEquals("john@releasepilot.com", result.getEmail());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.getUserByEmail("notfound@test.com"));
    }
}
