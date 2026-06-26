package com.cts.jfs.sprinboot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true, nullable = false)
    private String email;

    // Password field - stored as BCrypt hash
    @Column(nullable = false)
    private String password;

    private String phone;

    private Long productID;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    // ===== ENUMS =====

    public enum Role {
        ProductManager, DevLead, QAEngineer, ReleaseManager, CustomerAdmin, Admin
    }

    public enum UserStatus {
        Active, Inactive
    }

    // ===== NO-ARG CONSTRUCTOR (Required by JPA) =====
    public User() {}

    // ===== ALL-ARG CONSTRUCTOR =====
    public User(Long userID, String name, Role role, String email, String password,
                String phone, Long productID, UserStatus status) {
        this.userID = userID;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.productID = productID;
        this.status = status;
    }

    // ===== GETTERS & SETTERS =====

    public Long getUserID() { return userID; }
    public void setUserID(Long userID) { this.userID = userID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Long getProductID() { return productID; }
    public void setProductID(Long productID) { this.productID = productID; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }


}