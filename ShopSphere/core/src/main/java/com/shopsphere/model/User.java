package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_BLOCKED = "BLOCKED";

    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String mobile;
    private String role = ROLE_CUSTOMER;
    private String status = STATUS_ACTIVE;
    private Timestamp createdAt;

    public boolean isAdmin() { return ROLE_ADMIN.equals(role); }
    public boolean isActive() { return STATUS_ACTIVE.equals(status); }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
}
