package com.cts.jfs.sprinboot.controller;
 
import com.cts.jfs.sprinboot.model.User;
import com.cts.jfs.sprinboot.service.UserService;
 
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
 
    @Autowired
    private UserService userService;
 
    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
 
    // Get user by ID
    @GetMapping("/view/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
 
    // Save new user
    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }
 
    // Update user
    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
 
    // Delete user
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
 
    // Get users by role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(User.Role.valueOf(role)));
    }
 
    // Get users by product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<User>> getUsersByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(userService.getUsersByProduct(productId));
    }
 
    // Get users by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(userService.getUsersByStatus(User.UserStatus.valueOf(status)));
    }
}