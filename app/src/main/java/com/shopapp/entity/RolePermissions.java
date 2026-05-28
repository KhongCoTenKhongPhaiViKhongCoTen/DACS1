package com.shopapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name = "ROLE_PERMISSIONS")
@IdClass(RolePermissionsId.class)
public class RolePermissions implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Vaitro role;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Quyen permission;

    @Column(name = "granted_at", nullable = false, updatable = false)
    private LocalDateTime grantedAt;

    // Getters and Setters
    public Vaitro getRole() {
        return role;
    }

    public void setRole(Vaitro role) {
        this.role = role;
    }

    public Quyen getPermission() {
        return permission;
    }

    public void setPermission(Quyen permission) {
        this.permission = permission;
    }

    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(LocalDateTime grantedAt) {
        this.grantedAt = grantedAt;
    }
}