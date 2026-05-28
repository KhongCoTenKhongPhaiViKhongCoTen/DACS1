package com.shopapp.entity;

import java.io.Serializable;
import java.util.Objects;

public class RolePermissionsId implements Serializable {

    private Vaitro role;
    private Quyen permission;

    // Default constructor
    public RolePermissionsId() {}

    public RolePermissionsId(Vaitro role, Quyen permission) {
        this.role = role;
        this.permission = permission;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermissionsId that = (RolePermissionsId) o;
        return Objects.equals(role, that.role) && Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, permission);
    }
}